(ns vorrichtung-demo.components.grid.handlers
  (:require
    [ajax.core :refer [GET]]
    [re-frame.core :refer [register-handler trim-v enrich]]
    [vorrichtung.components.grid.middlewares :refer [init-data data-loaded order-by-column go-to-previous-page
                                                     go-to-next-page]]
    [vorrichtung-demo.components.grid.middlewares :refer [load-data]]))


(register-handler
  :grid/init-data
  (comp load-data init-data trim-v)
  (fn [db _]
    db))


(register-handler
  :grid/data-loaded
  (comp data-loaded trim-v)
  (fn [db _]
    db))


(register-handler
  :grid/order-by-column
  (comp load-data order-by-column trim-v)
  (fn [db _]
    (js/console.log db _)
    db))


(register-handler
  :grid/go-to-previous-page
  (comp load-data go-to-previous-page trim-v)
  (fn [db _]
    db))


(register-handler
  :grid/go-to-next-page
  (comp load-data go-to-next-page trim-v)
  (fn [db _]
    db))
