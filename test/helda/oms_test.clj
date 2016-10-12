(ns helda.oms-test
  (:require [clojure.test :refer :all]
            [helda.meta.core :refer :all]
            [helda.assembly.engines :refer :all]
            [helda.examples.oms :refer :all]))

(deftest matching-test
  (let
    [order {
      :tag :order
      :sym "IBM"
      :price 1.5
      :amount 1000
      :cp "Cp1"
      :buy-sell :buy
    }
    engine (run-oms :embedded)]
    (let [response (handle-msg engine order)]
      (testing "World changes"
        )
      )
    )
  )
