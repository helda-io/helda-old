(ns helda.handlers-test
  (:require [clojure.test :refer :all]
            [helda.examples.accounting :refer :all]
            [helda.meta.handlers :refer :all]
            ))

(deftest add-handler-test
  (let [meta (add-handler {} {
    :input-msg {:tag :msg-handler1 :txt "this is text msg"}
    :handler (fn [msg world] 2)
    })]
    (testing "Checking handler"
      (is (= 2 (handle {:tag :msg.handler1} meta {:meta meta})))
      )
    )
  )
