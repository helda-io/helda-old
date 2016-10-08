(ns helda.examples.oms
  (:require [schema.core :as s])
  (:use [helda.meta.core])
  (:use [helda.helpers.response])
  )

(def amount-tolerance 0.1)

(defn is-buy [order]
  (= :buy (order :buy-sell))
  )

(defn is-sell [order];buy
  (= :sell (order :buy-sell))
  )

(defn is-filled [order]
  (>= amount-tolerance (order :amount))
  )

(defn not-filled [order]
  (< amount-tolerance (order :amount))
  )

(defn opp-stack [order world]
  (if (is-buy order) (world :sell-stack) (world :buy-stack))
  )

(defn match-pair [order1 order2]
  (let [order1-price (order1 :price) order2-price (order2 :price)]
    (cond
      (= price1 price2) true
      (and (is-buy order1) (< order2-price order1-price)) true
      (and (is-sell order1) (> order2-price order1-price)) true
      :else false
      )
    )
  )

(defn match-stack [order stack]
  (if-let [order2 (first stack)]
    (if (match-pair order order2)
      order2
      (recur order (next stack))
      )
    )
  )

(defn fill-order [order world fills]
  (if (not-filled order)
    (if-let [order2 (match-stack order (opp-stack order world))]
      (let [
        fill-amount (min (order :amount) (order2 :amount))
        rest-order1 (withdraw-amount order fill-amount)
        rest-order2 (withdraw-amount order2 fill-amount)
        ]
        (recur
          (if (not-filled rest-order1) rest-order1 rest-order2)
          world
          (conj fills {:amount fill-amount :cp1 (order :cp) :cp2 (order2 :cp)})
          )
        )
        fills
      )
      fills
    )
  )

(defn create-meta []
  (-> (init-meta :accounts)
    (add-field {
      :name :buy-stack
      :default-value []
      :description "Bid orders stack"
      ; :schema s/Num
      })
    (add-field {
      :name :sell-stack
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
        :cp "Counterparty"
        :buy-sell "Bid/offer side"
        }
      :examples [
        {
          :tag :place-order
          :amount 100
          :price 12.5
          :cp "CP1"
          :buy-sell :offer
          }
        ]
      :msg-schema {
        :tag s/Keyword
        :amount s/Num
        :price s/Num
        :cp: s/Str
        :buy-sell (s/enum :bid :offer)
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
