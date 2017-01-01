(ns vorrichtung-demo.handlers
  (:require
    [re-frame.core :refer [reg-event-db]]
    [vorrichtung-demo.db :as db]))

(reg-event-db
 :initialize
 (fn  [_ _]
   db/default-db))


(reg-event-db
  :simple-control-click
  (fn [db [_ [el]]]
    (js/console.log _ el)
    db))
