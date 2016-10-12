(ns vorrichtung.components.grid.middlewares
  (:require
    [clojure.string :refer [join split]]
    [re-frame.core :refer [dispatch after enrich]]
    [ajax.core :refer [GET]]
    [vorrichtung.components.grid.db :refer [apply-next-state default-column-order next-order-state serialize-order
                                            compose-config-path headers->total previous-page? next-page? shift-page]]))


(defn init-data-handler
  "Inits grid's config in DB."
  [db [_ config]]
  (assoc-in db (compose-config-path config) config))


(def init-data (enrich init-data-handler))


(defn data-loaded-handler
  "Handles a loaded data in `response`."
  [db [_ config response headers]]
  (-> db
      (assoc-in [(:namespace config) (:id config) :data] response)
      (assoc-in (conj (compose-config-path config) :total) (headers->total headers))))


(def data-loaded (enrich data-loaded-handler))


(defn order-by-column-handler
  "Change grid's ordering by `column-name`."
  [db [_ config column-name]]
  (let [path [(:namespace config) (:id config) :config :order]
        current-order (get-in db path)
        column-order (get current-order column-name (default-column-order column-name))
        next-state (next-order-state (:ordering column-order))
        new-column-order (assoc column-order :ordering next-state)]
    (assoc-in db
              path
              (apply-next-state current-order
                                new-column-order))))


(def order-by-column (enrich order-by-column-handler))


(defn go-to-previous-page-handler
  [db [_ config]]
  (update-in db (compose-config-path config) (shift-page previous-page? -)))


(def go-to-previous-page (enrich go-to-previous-page-handler))


(defn go-to-next-page-handler
  [db [_ config]]
  (update-in db (compose-config-path config) (shift-page next-page? +)))


(def go-to-next-page (enrich go-to-next-page-handler))
