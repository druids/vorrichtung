(ns vorrichtung.arg-validation-test
  (:require [cljs.test :refer-macros [deftest testing are]]
            [vorrichtung.arg-validation :refer [validate-string]]))


(deftest validate-string-test
  (testing "should be valid"
    (are [string required] (= [string true] (validate-string string required))
         "" false
         nil false
         "asdf" false
         "asdf" true))

  (testing "should not be valid"
    (are [string required] (= [string false] (validate-string string required))
         "" true
         nil true)))
