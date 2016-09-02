(ns helda.adapters.dsl
  (:require [clojure.string :refer [split starts-with? trim]])
  (:require [helda.adapters.core :refer :all])
  )

(defn parse-params [tokens result]
  (if (< (count tokens) 2)
    (if (= 0 (count tokens))
      result
      (throw (Exception.
        "Unexpected statement, you need to provide pairs params value"))
      )
    (recur
      (next (next tokens)) ;Reading next key value
      (assoc result (first tokens) (second tokens)) ;Adding current key value
      )
    )
  )

(defn create-tokens [tokens result]
  (let [token (first tokens)]
    (if (not= (count tokens) 2)
      ;Last value can be empty because split could add "" when cmd finished with double quote (")
      (if (empty? (trim token)) result (conj result (first tokens)))
      (let [rest-tokens (trim (second tokens))]
        (if (starts-with? rest-tokens "\"")
          (recur
            (rest (split rest-tokens #"\"" 3)) ;we are calling rest here because split add empty string when split per double quote (")
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
