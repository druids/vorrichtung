(ns vorrichtung.components.grid.core
  (:require
    [clojure.set :refer [subset?]]
    [clojure.string :refer [join]]
    [goog.string.format]
    [goog.string :refer [format]]
    [reagent.core :as reagent]
    [re-frame.core :refer [dispatch subscribe]]
    [vorrichtung.components.grid.db :refer [args->config]]
    [vorrichtung.components.grid.views :refer [table thead th tbody tr td empty-grid]]
    [vorrichtung.core :refer [get-value]]))


(def grid-args-desc
  [{:name :url :required true :type :string}
   {:name :columns :required true :type :array}
   {:name :order :required false :type :array}
   {:name :namespace :required false :type :string}])


(def default-components-map
  {
   :table table
   :thead thead
   :th th
   :tbody tbody
   :tr tr
   :td td
   :empty-grid empty-grid
   })


(defn check-mapping
  [pattern-keys current-mapping]
  (do
    (when-not (subset? pattern-keys (set (keys current-mapping)))
      (js/console.error (format "Some required mapping keys missing for grid's mapping: %s"
                                (join ", " (map name pattern-keys)))))
    (when (some nil? (vals current-mapping))
      (js/console.error (format "Mapping values may not be nil, suspicious mapping: %s"
                                (map (fn [[k v]] (format "%s -> %s" k v)) current-mapping))))))


(defn check-grid-mapping
  [events-map subs-map components-map]
  (do
    (check-mapping #{:init-data} events-map)
    (check-mapping #{:grid-data} subs-map)
    (check-mapping (set (keys default-components-map)) components-map)))


(defn build-grid
  "A grid factory provides easier way how to customize the grid. In case that you need more customizability
   it's better to compose the grid by a hand.
   `events-map` maps grid's events into yours
   `subs-map` maps grid's subscribers into yours
   `components-map` maps grid's UI component into yours"
  [events-map subs-map components-map]
  (fn [el args]
    (let [init-config (args->config el args)]
      (reagent/create-class
        {:display-name "grid"
         :component-did-mount #(do
                                 (check-grid-mapping events-map subs-map components-map)
                                 (dispatch [(events-map :init-data) init-config]))
         :reagent-render (fn [_ _]
                           (let [grid-data (subscribe [(subs-map :grid-data)
                                                       [(:namespace init-config) (:id init-config)]])]
                             (fn [_ _]
                               (if (seq (:data @grid-data))
                                 [(:table components-map)
                                  @grid-data
                                  (:thead components-map)
                                  (:th components-map)
                                  (:tbody components-map)
                                  (:tr components-map)
                                  (:td components-map)]
                                 [(:empty-grid components-map)]))))}))))
