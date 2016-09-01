(ns helda.dsl-test
  (:require [clojure.test :refer :all]
            [helda.adapters.core :refer :all]
            [helda.adapters.dsl :refer :all]
            ))

(deftest correct-command-parsing
  (let [result (convert-input-msg
    (helda.adapters.dsl.DslMsgAdapter.)
    "command key1 value1 key2 value2"
    )]
    (testing "Checking parsed tokens"
      (is (= "command" (get result :tag)))
      (is (= "value1" (get result "key1")))
      (is (= "value2" (get result "key2")))
      )
    )
  )
