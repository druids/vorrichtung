(ns vorrichtung.components.grid.middlewares
  (:require
    [clojure.string :refer [join split]]
    [re-frame.core :refer [dispatch after enrich]]
    [ajax.core :refer [GET]]
    [vorrichtung.components.grid.db :refer [apply-next-state default-column-order next-order-state serialize-order
                                            compose-config-path headers->total previous-page? next-page? shift-page
                                            toggle-progress]]))


(defn init-data-handler
  "Inits grid's config in DB."
  [db [_ config]]
  (-> db
      (assoc-in (compose-config-path config) config)
      (toggle-progress config true)))


(def init-data (enrich init-data-handler))


(defn data-loaded-handler
  "Handles a loaded data in `response`."
  [db [_ config response headers]]
  (let [config-path (compose-config-path config)]
    (-> db
        (assoc-in [(:namespace config) (:id config) :data] response)
        (assoc-in (conj (compose-config-path config) :total) (headers->total headers))
        (toggle-progress config false))))


(def data-loaded (enrich data-loaded-handler))


(defn order-by-column-handler
  "Change grid's ordering by `column-name`."
  [db [_ config column-name]]
  (let [path [(:namespace config) (:id config) :config :order]
        current-order (get-in db path)
        column-order (get current-order column-name (default-column-order column-name))
        next-state (next-order-state (:ordering column-order))
        new-column-order (assoc column-order :ordering next-state)]
    (-> db
        (assoc-in path
                  (apply-next-state current-order
                                    new-column-order))
        (toggle-progress config true))))


(def order-by-column (enrich order-by-column-handler))


(defn go-to-previous-page-handler
  [db [_ config]]
  (-> db
      (update-in (compose-config-path config) (shift-page previous-page? -))
      (toggle-progress config true)))


(def go-to-previous-page (enrich go-to-previous-page-handler))


(defn go-to-next-page-handler
  [db [_ config]]
  (-> db
      (update-in (compose-config-path config) (shift-page next-page? +))
      (toggle-progress config true)))


(def go-to-next-page (enrich go-to-next-page-handler))
