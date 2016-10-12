(ns vorrichtung.components.grid.middlewares-test
  (:require
    [cljs.test :refer-macros [deftest testing are is]]
    [linked.core :as linked]
    [vorrichtung.components.grid.db :refer [Column Config ColumnOrder]]
    [vorrichtung.components.grid.middlewares :refer [init-data-handler data-loaded-handler order-by-column-handler
                                                     go-to-previous-page-handler go-to-next-page-handler]]))


(def testing-config (Config. "my-grid"
                             [(Column. "foo__name" "Foo" "foo" true) (Column. "bar" "Bar" "bar-alt" false)]
                             "/grid"
                             (linked/map "foo__name" (ColumnOrder. "foo__name" :ASC)
                                         "bar" (ColumnOrder. "bar" :DESC))
                             :VERBOSE
                             :grid
                             0
                             0
                             20
                             false))


(deftest init-data-handler-test

  (testing "should init grid's data"
    (is (= {:grid {
                   "my-grid" {
                              :config (assoc testing-config :progress? true)}}}
           (init-data-handler {} [nil testing-config])
           ))))


(deftest data-loaded-handler-test

  (testing "should store loaded data"
    (is (= {:grid {
                   "my-grid" {
                              :data [{:id "foo"}]
                              :config {:total 22
                                       :progress? false}}}}
           (data-loaded-handler {} [nil testing-config [{:id "foo"}] {:X-Total "22"}])
           ))))


(deftest order-by-column-handler-test

  (testing "should change ordering for a column `foo__name` to `DESC`"
    (is (= {:grid {
                   "my-grid" {
                              :config (-> testing-config
                                          (assoc-in [:order "foo__name" :ordering] :DESC)
                                          (assoc :progress? true))
                              :data [{:id "foo"}]}}}
           (order-by-column-handler {:grid {
                                            "my-grid" {
                                                       :config testing-config
                                                       :data [{:id "foo"}]}}}
                                    [nil testing-config "foo__name"])
         ))))


(deftest go-to-previous-page-handler-test

  (testing "should change a config to previous page"
    (is (= {:grid {"my-grid" {:config (merge testing-config {:offset 0
                                                             :total 22
                                                             :base 10
                                                             :progress? true})}}}
           (go-to-previous-page-handler {:grid {"my-grid" {:config (merge testing-config {:offset 0
                                                                                          :base 10
                                                                                          :total 22})}}}
                                        [nil testing-config])))))


(deftest go-to-next-page-handler-test

  (testing "should change a config to next page"
    (is (= {:grid {"my-grid" {:config (merge testing-config {:offset 10
                                                             :total 22
                                                             :base 10
                                                             :progress? true})}}}
           (go-to-next-page-handler {:grid {"my-grid" {:config (merge testing-config {:offset 0
                                                                                      :base 10
                                                                                      :total 22})}}}
                                    [nil testing-config])))))
