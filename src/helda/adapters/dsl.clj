(ns helda.adapters.dsl
  (:require [clojure.string :refer [split starts-with?]])
  (:require [helda.adapters.core :refer :all])
  )

(def ws-regex #"[\s;:=,{}]+")

(defn parse-value [value-str]
  (try
    (Long/parseLong value-str)
    (catch Exception e value-str)
    )
  )

(defn parse-params [tokens result]
  (if (< (count tokens) 2)
    (if (= 0 (count tokens))
      result
      (throw (Exception.
        "Unexpected statement, you need to provide pairs params value"))
      )
    (recur
      (drop 2 tokens) ;Reading next {key value}
      (assoc result (keyword (first tokens)) (parse-value (second tokens)))
      )
    )
  )

(defn create-tokens [tokens result]
  (if (not= (count tokens) 2)
    (conj result (first tokens))
    (let [rest-tokens (second tokens)]
      (if (starts-with? rest-tokens "\"")
        (let [str-split (split rest-tokens #"\"" 3)
              head-res (second str-split) ;split adds empty string when split per double quote ("), that's why we start reading from second. That's kind of advanced triml
              tail-res (second (split (nth str-split 2) ws-regex 2))
          ]
          (recur
            (if (empty? tail-res)
              [head-res]
              [head-res tail-res]
              )
            (conj result (first tokens))
            )
          )
        (recur
          (split rest-tokens ws-regex 2)
          (conj result (first tokens))
          )
        )
      )
    )
  )

(deftype DslMsgAdapter []
  MsgAdapter

  (convert-input-msg [this msg]
    (parse-params (create-tokens ["tag" msg] []) {})
    )

  (convert-results [this msg]
    msg
    )
  )
