(ns vorrichtung-demo.components.grid.handlers
  (:require
    [ajax.core :refer [GET]]
    [re-frame.core :refer [reg-event-db enrich]]
    [vorrichtung.components.grid.middlewares :refer [init-data data-loaded order-by-column go-to-previous-page
                                                     go-to-next-page]]
    [vorrichtung-demo.components.grid.middlewares :refer [load-data]]))


(reg-event-db
  :grid/init-data
  [load-data init-data]
  (fn [db _]
    db))


(reg-event-db
  :grid/data-loaded
  [data-loaded]
  (fn [db _]
    db))


(reg-event-db
  :grid/order-by-column
  [load-data order-by-column]
  (fn [db _]
    (js/console.log db _)
    db))


(reg-event-db
  :grid/go-to-previous-page
  [load-data go-to-previous-page]
  (fn [db _]
    db))


(reg-event-db
  :grid/go-to-next-page
  [load-data go-to-next-page]
  (fn [db _]
    db))
