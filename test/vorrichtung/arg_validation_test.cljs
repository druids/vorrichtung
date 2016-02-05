(ns vorrichtung.arg-validation-test
  (:require [cljs.test :refer-macros [deftest testing are]]
            [vorrichtung.arg-validation :refer [validate-string validate-array validate-object validate-int]]))


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


(deftest validate-array-test
  (testing "should be valid"
    (are [value required parsed] (= [parsed true] (validate-array value required))
         nil false nil
         "" false nil
         "[]" false []
         "[]" true []
         "[1, 2]" false [1 2]
         "[1, 2]" true [1 2]))

  (testing "should not be valid"
    (are [value required] (= [value false] (validate-array value required))
         "invalid-value" true
         nil true
         "" true
         "[" true
         "{}" true)))


(deftest validate-object-test
  (testing "should be valid"
    (are [value required parsed] (= [parsed true] (validate-object value required))
         nil false nil
         "" false nil
         "{}" false {}
         "{}" true {}
         "{\"a\": \"1\", \"b\": \"2\"}" false {"a" "1" "b" "2"}
         "{\"a\": \"1\", \"b\": \"2\"}" true {"a" "1" "b" "2"}))

  (testing "should not be valid"
    (are [value required] (= [value false] (validate-object value required))
         "invalid-value" true
         nil true
         "" true
         "{" true
         "[]" true)))


(deftest validate-int-test
  (testing "should be valid"
    (are [integer required parsed] (= [parsed true] (validate-int integer required))
         "" false nil
         nil false nil
         "10" false 10
         "-10" true -10))

  (testing "should not be valid"
    (are [integer required] (= [nil false] (validate-int integer required))
         "" true
         nil true
         "as" false
         "as" true)))
