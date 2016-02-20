(ns vorrichtung.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [goog.dom]
            [vorrichtung.dom :as dom]
            [vorrichtung.core :refer [all-args-are-valid? format-invalid-args process-control render-component]]))


(deftest all-args-are-valid?-test
  (testing "all args should be valid"
    (is (all-args-are-valid? {:arg1-name ["arg1-value" true]
                              :arg2-name ["arg2-value" true]})))

  (testing "any arg should not be valid"
    (is (not (all-args-are-valid? {:arg1-name ["arg1-value" true]
                                   :arg2-name ["arg2-value" false]})))))


(deftest format-invalid-args-test
  (testing "should format invalid args"
    (is (= (format-invalid-args
             [{:name :foo :type :string :required true}
              {:name :bar :type :string :required false}
              {:name :bla :type :string :required true}]
             {:foo ["foo" false]
              :bar ["bar" true]
              :bla ["bla" false]})
           ["foo -> 'foo'" "bla -> 'bla'"]))))


(defn create-and-append-el
  [tag klass]
  (let [el (goog.dom/createDom tag (clj->js {:className klass
                                             :id klass}))]
    (do
      (goog.dom/appendChild (.-body js/document) el)
      el)))


(deftest process-control-test
  (testing "should process a component"
    (-> (process-control
          render-component
          "component"
          ".bar"
          [(fn [_ _] [:div "Hello"])
           [{:name :bar
             :type :string
             :required false}]]
          (create-and-append-el "div" "bar"))

        nil?
        not
        is)

    (-> (process-control
          render-component
          "component"
          ".bar"
          [(fn [_ _] [:div "Hello"])
           [{:name :bar
             :type :string
             :required true}]]
          (create-and-append-el "div" "bar"))

        nil?
        is)))


(deftest render-component-test
  (testing "should render a component"
    (-> (render-component
          ".bar"
          (fn [_ _] [:div "Hello"])
          (create-and-append-el "div" "bar")
          {:name [nil true]})

        nil?
        not
        is))

  (testing "should render a component with let"
    (-> (render-component
          ".bar"
          (fn [_ _] (let [a 0] (fn [] [:div "Hello" a])))
          (create-and-append-el "div" "bar")
          {:name [nil true]})

        nil?
        not
        is)))
