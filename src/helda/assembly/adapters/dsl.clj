(ns helda.assembly.adapters.dsl
  (:require [clojure.string :refer [split starts-with? capitalize]])
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

(defn map2seq [m level] (map #(conj % level) (seq m)))

(defn tuple-key [tuple] (nth tuple 0))
(defn tuple-value [tuple] (nth tuple 1))
(defn nested-map? [tuple] (map? (tuple-value tuple)))
(defn tuple-level [tuple] (nth tuple 2))

(defn tuple-indent [tuple]
  (apply str (repeat (tuple-level tuple) "  "))
  )

(defn render-attr [in out]
  (if-let [tuple (first in)]
    (recur
      (if (nested-map? tuple)
        (concat
          (map2seq (tuple-value tuple) (inc (tuple-level tuple)))
          (next in)
          )
        (next in)
        )
      (str out (tuple-indent tuple) (tuple-key tuple) ": "
        (if (nested-map? tuple) "" (tuple-value tuple)) "\n")
      )
    out
    )
  )


(def msg {:tag :commands,
  :commands {:accounts
    {
      :get-accounts {},
      :accounting-entry {:input-msg {:debit "Debit account field name", :credit "Credit account field name", :amount "Money amount"}, :examples [{:tag :accounting-entry, :debit :account-assets-fixed,
  :credit :account-owner-equities, :amount 1000}]}}}})

(render-attr (map2seq msg 0) "")

(deftype DslMsgAdapter []
  MsgAdapter

  (convert-input-msg [this msg]
    (parse-params (create-tokens ["tag" msg] []) {})
    )

  (convert-results [this msg]
    (render-attr (map2seq msg 0) "")
    )
  )
