(ns helda.adapters.dsl
  (:require [clojure.string :refer [split starts-with? trim]])
  (:require [helda.adapters.core :refer :all])
  )

(defn parse-params [tokens result]
  (if (< (count tokens) 2)
    (if (= 0 (count tokens))
      result
      (throw (Exception. "Unexpected statement, you need to provide pairs params value"))
      )
    (recur (next (next tokens)) (assoc result (first tokens) (second tokens)))
    )
  )

(defn create-tokens [tokens result]
  (if (not= (count tokens) 2)
    (conj result (first tokens))
    (let [rest-tokens (trim (second tokens))]
      (if (starts-with? rest-tokens "\"")
        (recur
          (rest (split rest-tokens #"\"" 3))
          (conj result (first tokens))
          )
        (recur
          (split rest-tokens #" " 2)
          (conj result (first tokens))
          )
        )
      )
    )
  )

(deftype DslMsgAdapter []
  MsgAdapter

  (convert-input-msg [this msg]
    (parse-params (create-tokens (list "tag" msg) []) {})
    )

  (convert-results [this msg]
    msg
    )
  )
