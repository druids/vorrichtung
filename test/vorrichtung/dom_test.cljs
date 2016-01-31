(ns vorrichtung.dom-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [goog.dom]
            [vorrichtung.dom :as dom]))


(deftest dom-all-test
  (testing "should return selected elements as a sequence"
    (doseq [_ (range 5)]
      (goog.dom/appendChild
        (.-body js/document)
        (goog.dom/createDom "div" "foo")))
    (let [els (dom/all ".foo")]
      (is (= 5 (count els)))
      (is (seq? els)))))
