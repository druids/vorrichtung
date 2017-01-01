(ns vorrichtung.components.grid.subs)


(defn subscribe-grid-data
  [db [_ path]]
  (get-in db path))
