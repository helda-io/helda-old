(ns helda.dsl-test
  (:require [clojure.test :refer :all]
            [helda.adapters.core :refer :all]
            [helda.adapters.dsl :refer :all]
            ))

(deftest basic-command-parsing
  (let [result (convert-input-msg
    (helda.adapters.dsl.DslMsgAdapter.)
    "command key1 value1 key2 2"
    )]
    (testing "Checking parsed tokens"
      (is (= :command (get result :tag)))
      (is (= "value1" (get result :key1)))
      (is (= "2" (get result :key2)))
      )
    )
  )

(deftest quoted-command-parsing
  (let [result (convert-input-msg
    (helda.adapters.dsl.DslMsgAdapter.)
    "command key0 \"123 456\" key3 v4 key1 \" key2 value2 \""
    )]
    (testing "Checking parsed tokens"
      (is (= :command (get result :tag)))
      (is (= " key2 value2 " (get result :key1)))
      (is (= "v4" (get result :key3)))
      (is (= "123 456" (get result :key0)))
      )
    )
  )

(deftest syntax-sugar
  (let [result (convert-input-msg
    (helda.adapters.dsl.DslMsgAdapter.)
    "command key0 \"123 456\", key3=v4, key1=\" key2 value2 \""
    )]
    (testing "Checking parsed tokens"
      (is (= :command (get result :tag)))
      (is (= " key2 value2 " (get result :key1)))
      (is (= "v4" (get result :key3)))
      (is (= "123 456" (get result :key0)))
      )
    )
  )
