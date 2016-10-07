(ns helda.examples.oms
  (:require [schema.core :as s])
  (:use [helda.meta.core])
  (:use [helda.helpers.response])
  )

(defn is-bid [msg];sell
  (= :bid (msg :bid-offer))
  )

(defn is-offer [msg];buy
  (= :offer (msg :bid-offer))
  )

(defn opp-stack [msg world]
  (if (is-bid msg) (world :ask-stack) (world :bid-stack))
  )

(defn match [msg1 msg2]
  (let [msg1-price (msg1 :price) msg2-price (msg2 :price)]
    (cond
      (= price1 price2) true
      (and (is-bid msg1) (> msg2-price msg1-price)) true
      (and (is-offer msg1) (< msg2-price msg1-price)) true
      :else false
      )
    )
  )

(defn create-meta []
  (-> (init-meta :accounts)
    (add-field {
      :name :bid-stack
      :default-value []
      :description "Bid orders stack"
      ; :schema s/Num
      })
    (add-field {
      :name :ask-stack
      :default-value []
      :description "Ask orders stack"
      ; :schema s/Num
      })
    (add-handler {
      :tag :place-order
      :input-msg {
        :sym "Symbol"
        :amount "Money amount"
        :price "Price"
        :bid-offer "Bid/offer side"
        }
      :examples [
        {
          :tag :place-order
          :amount 100
          :price 12.5
          :bid-offer :offer
          }
        ]
      :msg-schema {
        :tag s/Keyword
        :amount s/Num
        :price s/Num
        :bid-offer (s/enum :bid :offer)
        }
      :handler (fn [msg world]
        (if (is-bid msg)

        )
      })
    )
  )

(defn run-accounting [adapter]
  (-> (init-assembly adapter)
    (add-meta (create-meta))
    run-assembly
    )
  )
