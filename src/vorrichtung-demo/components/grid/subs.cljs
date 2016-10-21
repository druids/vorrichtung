(ns vorrichtung-demo.components.grid.subs
  (:require-macros
    [reagent.ratom :refer [reaction]])
  (:require
    [re-frame.core :refer [register-sub]]
    [vorrichtung.components.grid.subs :refer [subscribe-grid-data]]))


(register-sub
  :grid/grid-data
  subscribe-grid-data)
