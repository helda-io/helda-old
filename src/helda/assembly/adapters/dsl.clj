(ns helda.assembly.adapters.dsl
  (:require [clojure.string :refer [split starts-with?]])
  (:require [helda.assembly.adapters.core :refer :all])
  )

(def ws-regex #"[\s;=,{}]+")

(defn parse-key-value [result-map key-str value-str]
  (assoc
    result-map
    (keyword key-str)
    (if (or (= key-str "tag") (= key-str "world"))
      (keyword value-str)
      value-str
      )
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
      (parse-key-value result (first tokens) (second tokens))
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

(defn render-attr [key attr level]
  (if (and (map? attr) (not (= key :msg-schema)))
    (let [splitter (str "\n" (apply str (repeat level "\t")))]
      (reduce
        #(str %1 splitter %2)
        ""
        (map
          #(str % ": " (render-attr % (attr %) (inc level)))
          (keys attr)
          )
        )
      )
    attr
    )
  )

(deftype DslMsgAdapter []
  MsgAdapter

  (convert-input-msg [this msg]
    (parse-params (create-tokens ["tag" msg] []) {})
    )

  (convert-results [this msg]
    (reduce #(str %1 "\n" %2)
      (str "========== " (msg :tag) " ==========")
      (map #(str % ": " (render-attr % (msg %) 1)) (keys (dissoc msg :tag)))
      )
    )
  )
