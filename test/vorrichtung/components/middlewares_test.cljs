(ns vorrichtung.components.grid.middlewares-test
  (:require
    [cljs.test :refer-macros [deftest testing are is]]
    [linked.core :as linked]
    [vorrichtung.components.grid.db :refer [Column Config ColumnOrder]]
    [vorrichtung.components.grid.middlewares :refer [init-data-handler data-loaded-handler order-by-column-handler]]))


(def testing-config (Config. :grid
                             [(Column. "foo__name" "Foo" "foo" true) (Column. "bar" "Bar" "bar-alt" false)]
                             "/grid"
                             (linked/map "foo__name" (ColumnOrder. "foo__name" :ASC)
                                         "bar" (ColumnOrder. "bar" :DESC))
                             :VERBOSE
                             "my-grid"))


(deftest init-data-handler-test

  (testing "should init grid's data"
    (is (= {"my-grid" {
                       :grid {
                              :config testing-config}}}
           (init-data-handler {} [nil testing-config])
           ))))


(deftest data-loaded-handler-test

  (testing "should store loaded data"
    (is (= {"my-grid" {
                       :grid {
                              :data [{:id "foo"}]}}}
           (data-loaded-handler {} [nil testing-config [{:id "foo"}]])
           ))))


(deftest order-by-column-handler-test

  (testing "should change ordering for a column `foo__name` to `DESC`"
    (is (= {"my-grid" {
                       :grid {
                              :config (assoc-in testing-config [:order "foo__name" :ordering] :DESC)
                              :data [{:id "foo"}]}}}
           (order-by-column-handler {"my-grid" {
                                                :grid {
                                                       :config testing-config
                                                       :data [{:id "foo"}]}}}
                                    [nil testing-config "foo__name"])
         ))))
