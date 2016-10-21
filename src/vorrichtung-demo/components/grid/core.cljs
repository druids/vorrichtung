(ns vorrichtung-demo.components.grid.core
  (:require
    [vorrichtung.core :refer [register-component]]
    [vorrichtung.components.grid.core :refer [build-grid grid-args-desc default-components-map Mappings]]
    [vorrichtung-demo.components.grid.handlers]
    [vorrichtung-demo.components.grid.subs]))


(def events-map
  {
   :init-data :grid/init-data
   :order-by-column :grid/order-by-column
   :go-to-previous-page :grid/go-to-previous-page
   :go-to-next-page :grid/go-to-next-page
   })


(def subs-map
  {
   :grid-data :grid/grid-data
   })


(register-component ".grid" [(build-grid (Mappings. events-map subs-map default-components-map)) grid-args-desc])
