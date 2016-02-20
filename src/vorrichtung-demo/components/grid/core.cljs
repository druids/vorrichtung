(ns vorrichtung-demo.components.grid.core
  (:require
    [vorrichtung.core :refer [register-component]]
    [vorrichtung.components.grid.core :refer [build-grid grid-args-desc default-components-map]]
    [vorrichtung-demo.components.grid.handlers]
    [vorrichtung-demo.components.grid.subs]))


(def events-map
  {:init-data :grid/init-data})


(def subs-map
  {:grid-data :grid/grid-data})


(register-component ".grid" [(build-grid events-map subs-map default-components-map) grid-args-desc])
