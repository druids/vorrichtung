(ns vorrichtung.components.grid.db-test
  (:require
    [cljs.test :refer-macros [deftest testing are is]]
    [linked.core :as linked]
    [vorrichtung.components.grid.db :refer [serialized-order->order ColumnOrder unserialize-order args->config Config Column
                                            nested-field->header-field config->header-fields next-order-state
                                            apply-next-state config->order-class column-order->serialized-order
                                            serialize-order order->order-column-map compose-config-path item-value]]))


(deftest serialized-order->order-test

  (testing "should unserialize an order"
    (are [expected serialized-order] (= expected (serialized-order->order serialized-order))

         (ColumnOrder. "foo" :DESC)
         "-foo"

         (ColumnOrder. "bar" :ASC)
         "bar"

         (ColumnOrder. "" :ASC)
         ""
         )))


(deftest unserialize-order-test

  (testing "should unserialize order"
    (are [expected serialized-order] (= expected (unserialize-order serialized-order))

         (linked/map "foo" (ColumnOrder. "foo" :ASC)
                     "bar" (ColumnOrder. "bar" :DESC))
         ["foo" "-bar"]

         (linked/map)
         []
         )))


(def testing-config (Config. :grid
                             [(Column. "foo__name" "Foo" "foo" true) (Column. "bar" "Bar" "bar-alt" false)]
                             "/grid"
                             (linked/map "foo__name" (ColumnOrder. "foo__name" :ASC)
                                         "bar" (ColumnOrder. "bar" :DESC))
                             :VERBOSE
                             "my-grid"))


(deftest args->config-test

  (testing "should convert arguments into a config"
    (is (= testing-config
           (args->config (js-obj "id" "grid")
                         {
                          :columns [
                                    [
                                     {:id "foo__name" :verbose_name "Foo" :orderable_by "foo" :safe_html true}
                                     {:id "bar" :verbose_name "Bar" :orderable_by "bar-alt" :safe_html false}
                                    ]
                                    true
                                   ]
                          :order [["foo__name" "-bar"] true]
                          :namespace ["my-grid" true]
                          :url ["/grid" true]
                          })
           ))))


(deftest nested-field->header-field-test

  (testing "should convert a nested field to a header field"
    (are [expected nested-field] (nested-field->header-field nested-field)

         "name"
         "name"

         "person(name)"
         "person__name"

         ""
         ""
         )))


(deftest config->header-fields-test

  (testing "should convert a config to header fields"
    (are [expected config] (= expected (config->header-fields config))

         "id,_obj_name,_rest_links,_actions,_class_names,_web_links,_default_action,foo(name),bar"
         testing-config

         "id,_obj_name,_rest_links,_actions,_class_names,_web_links,_default_action"
         (assoc testing-config :columns [])

         )))


(deftest next-order-state-test

  (testing "should compute next order state"
    (are [expected ordering-direction] (= expected (next-order-state ordering-direction))

         :DESC :ASC
         :NONE :DESC
         :ASC :NONE
         :NONE :foo

         )))


(deftest apply-next-state-test

  (testing "should apply next state"
    (are [expected order column-order] (= expected (apply-next-state order column-order))

         (linked/map "foo" (ColumnOrder. "foo" :ASC) ; change ordering of `bar` to DESC
                     "bar" (ColumnOrder. "bar" :DESC))
         (linked/map "foo" (ColumnOrder. "foo" :ASC)
                     "bar" (ColumnOrder. "bar" :ASC))
         (ColumnOrder. "bar" :DESC)

         (linked/map "foo" (ColumnOrder. "foo" :ASC)) ; remove ordering of `bar`
         (linked/map "foo" (ColumnOrder. "foo" :ASC)
                     "bar" (ColumnOrder. "bar" :DESC))
         (ColumnOrder. "bar" :NONE)

         (linked/map "foo" (ColumnOrder. "foo" :ASC) ; add ordering of `bar`
                     "bar" (ColumnOrder. "bar" :ASC))
         (linked/map "foo" (ColumnOrder. "foo" :ASC))
         (ColumnOrder. "bar" :ASC)

         (linked/map "foo" (ColumnOrder. "foo" :ASC)) ; unknown ordering of `foo`
         (linked/map "foo" (ColumnOrder. "foo" :ASC))
         (ColumnOrder. "foo" :foo)

         )))


(def testing-ordering->class
  {
   :ASC "ascending"
   :DESC "descending"
   :NONE nil
   })


(deftest config->order-class-test

  (testing "should return a CSS class according to ordering"
    (are [expected config orderable-by ordering->class] (= expected (config->order-class config
                                                                                         orderable-by
                                                                                         ordering->class))
         "ascending"
         testing-config
         "foo__name"
         testing-ordering->class

         "descending"
         testing-config
         "bar"
         testing-ordering->class

         nil
         testing-config
         "foo"
         testing-ordering->class

         )))


(deftest column-order->serialized-order-test

  (testing "should serialize column's order"
    (are [expected column-order] (= expected (column-order->serialized-order [0 column-order]))

         "-foo"
         (ColumnOrder. "foo" :DESC)

         "bar"
         (ColumnOrder. "bar" :ASC)

         ""
         nil

         )))


(deftest serialize-order-test

  (testing "should serialize order"
    (are [expected order] (= expected (serialize-order order))

         "foo,-bar"
         (linked/map "foo" (ColumnOrder. "foo" :ASC)
                     "bar" (ColumnOrder. "bar" :DESC))

         ""
         (linked/map)

         )))


(deftest order->order-column-map-test

  (testing "should converts order to ordered map"
    (are [expected order] (= expected (order->order-column-map order))

         {"foo" 1
          "bar" 2}
         (linked/map "foo" (ColumnOrder. "foo" :ASC)
                     "bar" (ColumnOrder. "bar" :DESC))

         {}
         {}

         )))


(deftest compose-config-path-test

  (testing "should compose a config path"
    (is (= ["my-grid" :grid :config] (compose-config-path testing-config))
        )))


(deftest item-value-test

  (testing "should return item's value"
    (are [expected item column] (= expected (item-value item column))

         "bar"
         {:foo "bar"}
         (Column. "foo" "Foo" "foo" true)


         "bar"
         {:foo {:name "bar"}}
         (Column. "foo__name" "Foo" "foo" true)

         )))
