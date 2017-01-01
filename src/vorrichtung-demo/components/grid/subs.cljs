(ns vorrichtung-demo.components.grid.subs
  (:require
    [re-frame.core :refer [reg-sub]]
    [vorrichtung.components.grid.subs :refer [subscribe-grid-data]]))


(reg-sub
  :grid/grid-data
  subscribe-grid-data)
