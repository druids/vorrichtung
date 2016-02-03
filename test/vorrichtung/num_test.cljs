(ns vorrichtung.num-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [vorrichtung.num :refer [str->int]]))


(deftest str->int-test
  (testing "should parse a decimal"
    (is (= 10 (str->int "10"))))

  (testing "should parse a negative decimal"
    (is (= -10 (str->int "-10"))))

  (testing "should return nil"
    (is (nil? (str->int "asdf")))))
