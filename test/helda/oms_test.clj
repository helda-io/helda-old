(ns helda.oms-test
  (:require [clojure.test :refer :all]
            [helda.meta.core :refer :all]
            [helda.assembly.engines :refer :all]
            [helda.examples.oms :refer :all]))

(deftest matching-test
  (let
    [order-buy {
      :tag :order
      :sym "IBM"
      :price 1.5
      :amount 1000
      :cp "Cp1"
      :buy-sell :buy
    }
    order-sell {
      :tag :order
      :sym "IBM"
      :price 1.5
      :amount 1000
      :cp "Cp1"
      :buy-sell :sell
    }
    engine (run-oms :embedded)]
    (handle-msg engine order-buy)
    (let [response (handle-msg engine order-sell)]
      (testing "World changes"
        )
      )
    )
  )
