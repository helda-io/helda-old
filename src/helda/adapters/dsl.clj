(ns helda.adapters.dsl
  (:require [clojure.string :refer [split]])
  (:require [helda.adapters.core :refer :all])
  )

(defn parse-params [tokens result]
  (if (< (count tokens) 2)
    (if (= 0 (count tokens))
      result
      (throw (.Exception (str "Unexpected params in command: " tokens)))
      )
    (recur (next (next tokens)) (assoc result (first tokens) (second tokens)))
    )
  )

(deftype DslMsgAdapter []
  MsgAdapter

  (convert-input-msg [this msg]
    (let [tokens (split msg #" ")]
      (parse-params (rest tokens) {:tag (first tokens)})
      )
    )

  (convert-results [this msg]
    msg
    )
  )
