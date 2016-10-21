(ns vorrichtung.components.grid.core
  (:require
    [clojure.set :refer [subset?]]
    [clojure.string :refer [join]]
    [goog.string.format]
    [goog.string :refer [format]]
    [reagent.core :as reagent]
    [re-frame.core :refer [dispatch subscribe]]
    [vorrichtung.components.grid.db :refer [args->config]]
    [vorrichtung.components.grid.views :refer [table thead th tbody tr td empty-grid paginator]]
    [vorrichtung.core :refer [get-value]]))


(def grid-args-desc
  [{:name :url :required true :type :string}
   {:name :columns :required true :type :array}
   {:name :order :required false :type :array}
   {:name :namespace :required false :type :string}
   {:name :base :require false :type :int}])


(def default-components-map
  {
   :table table
   :thead thead
   :th th
   :tbody tbody
   :tr tr
   :td td
   :empty-grid empty-grid
   :paginator paginator
   })


(def events-mapping-keys #{:init-data :order-by-column :go-to-previous-page :go-to-next-page})

(def subs-mapping-keys #{:grid-data})


(defn check-mapping
  [pattern-keys current-mapping]
  (do
    (when-not (subset? pattern-keys (set (keys current-mapping)))
      (js/console.error (format "Some required mapping keys missing for grid's mapping: %s"
                                (join ", " (map name pattern-keys)))))
    (when (some nil? (vals current-mapping))
      (js/console.error (format "Mapping values may not be nil, suspicious mapping: %s"
                                (map (fn [[k v]] (format "%s -> %s" k v)) current-mapping))))))


(defn check-grid-mappings
  [mappings]
  (do
    (check-mapping events-mapping-keys (:events mappings))
    (check-mapping subs-mapping-keys (:subs mappings))
    (check-mapping (set (keys default-components-map)) (:components mappings))))


(defrecord Mappings [events subs components])


(defn build-grid
  "A grid factory provides easier way how to customize the grid. In case that you need more customizability
   it's better to compose the grid by a hand. The factory takes a `record` called `Mappings` which has following  keys:
   `events` maps grid's events into yours
   `subs` maps grid's subscribers into yours
   `components` maps grid's UI component into yours"
  [mappings]
  (fn [el args]
    (let [init-config (args->config el args)]
      (reagent/create-class
        {:display-name "grid"
         :component-did-mount #(do
                                 (check-grid-mappings mappings)
                                 (dispatch [(get-in mappings [:events :init-data]) init-config]))
         :reagent-render (fn [_ _]
                           (let [grid-data (subscribe [(get-in mappings [:subs :grid-data])
                                                       [(:namespace init-config) (:id init-config)]])]
                             (fn [_ _]
                               (if (seq (:data @grid-data))
                                 [:div.table-wrapper
                                  [(get-in mappings [:components :table])
                                   @grid-data
                                   (:thead (:components mappings))
                                   (:th (:components mappings))
                                   (:tbody (:components mappings))
                                   (:tr (:components mappings))
                                   (:td (:components mappings))
                                   mappings]
                                  [(get-in mappings [:components :paginator]) (:config @grid-data) mappings]]
                                 [(:empty-grid (:components mappings))]))))}))))
