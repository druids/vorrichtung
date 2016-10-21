(ns vorrichtung.components.grid.subs
  (:require-macros
    [reagent.ratom :refer [reaction]]))


(defn subscribe-grid-data
  [db [_ path]]
  (reaction (get-in @db path nil)))
