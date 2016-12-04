(ns vorrichtung.num-test
  (:require
    [cljs.test :refer-macros [deftest testing are]]
    [vorrichtung.num :refer [str->int str->float]]))


(deftest str->int-test

  (testing "should parse an integer"

    (are [expected value] (= expected (str->int value))

         10 "10"
         -10 "-10"
         nil "asdf"
         nil "100.00"
         nil "100."

         )))


(deftest str->float-test

  (testing "should parse a float"

    (are [expected value] (= expected (str->float value))

         10 "10"
         -10 "-10"
         nil "asdf"
         100.01 "100.01"
         -100.01 "-100.01"
         nil "100."

         )))
