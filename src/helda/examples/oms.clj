(ns helda.examples.oms
  (:require [schema.core :as s])
  (:use [helda.meta.core])
  (:use [helda.helpers.response])

  (:require [helda.assembly.core :refer :all])
  )

(def amount-tolerance 0.1)

(defn is-buy [order] (-> order :buy-sell (= :buy)))

(defn is-sell [order] (-> order :buy-sell (= :sell)))

(defn is-filled [order] (->> order :amount (>= amount-tolerance)))

(defn stack-key [order] (if (is-buy order) :buy-stack :sell-stack))

(defn opp-stack-key [order] (if (is-buy order) :sell-stack :buy-stack))

(defn match-pair [order1 order2]
  (let [order1-price (order1 :price) order2-price (order2 :price)]
    (cond
      (= order1-price order2-price) true
      (and (is-buy order1) (< order2-price order1-price)) true
      (and (is-sell order1) (> order2-price order1-price)) true
      :else false
      )
    )
  )

(defn match-stack [order stack] ;we are trying only first because list sorted
  (if-let [order2 (first stack)] (match-pair order order2))
  )

(defn withdraw-amount [order amount]
  (->> amount
    (- (order :amount))
    (assoc order :amount)
    )
  )

(defn insert-order [order col]
  (if-not (is-filled order)
    (-> col
      (conj order)
      (sort-by :price (if (is-buy order) < >))
      )
    col
    )
  )

(defn fill-order [order world changes]
  (if-not (is-filled order)
    (let [opp-stack ((or changes world) (opp-stack-key order))]
      (if-let [order2 (match-stack order opp-stack)]
        (let [fill-amount (min (order :amount) (order2 :amount))]
          (recur
            (withdraw-amount order fill-amount)
            world
            {
              :fills (conj (changes :fills)
                {:amount fill-amount :cp1 (order :cp) :cp2 (order2 :cp)}
                )
              (opp-stack-key order)
                (->> opp-stack
                  (remove #(= order2 %))
                  (insert-order (withdraw-amount order2 fill-amount))
                  )
            })
          )
          (assoc changes (stack-key order)
            (insert-order order (-> order stack-key world))
            )
        )
      )
      changes
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
    (add-field {
      :name :fills
      :default-value []
      :description "Order fills"
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
        :cp s/Str
        :buy-sell (s/enum :bid :offer)
        }
      :handler (fn [msg world]
        (fill-order msg world nil)
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
