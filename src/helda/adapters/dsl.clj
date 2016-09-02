(ns helda.adapters.dsl
  (:require [clojure.string :refer [split starts-with? triml]])
  (:require [helda.adapters.core :refer :all])
  )

(def ws-regex #"[\s;:=,{}]+")

(defn parse-params [tokens result]
  (if (< (count tokens) 2)
    (if (= 0 (count tokens))
      result
      (throw (Exception.
        "Unexpected statement, you need to provide pairs params value"))
      )
    (recur
      (next (next tokens)) ;Reading next {key value}
      (assoc result (first tokens) (second tokens)) ;Adding current {key value}
      )
    )
  )

(defn create-tokens [tokens result]
  (if (not= (count tokens) 2)
    (if (empty? (triml (first tokens))) result (conj result (first tokens))) ;Last value can be empty because split could add "" when cmd finished with double quote (")
    (let [rest-tokens (triml (second tokens))] ;We need to trim because if we split per double quote (") on previous iteration - it could add empty string as first element
      (if (starts-with? rest-tokens "\"")
        (let [str-split (split rest-tokens #"\"" 3)
              head-res (second str-split) ;split adds empty string when split per double quote ("), that's why second
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
