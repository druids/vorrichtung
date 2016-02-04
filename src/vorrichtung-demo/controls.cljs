(ns vorrichtung-demo.controls
  (:require [re-frame.core :refer [dispatch]]
            [goog.events :as events]))


(defn simple-control
  "Listens for `CLICK` event. Renders nothing."
  [el parsed-args]
  (events/listen el goog.events.EventType.CLICK #(dispatch [:simple-control-click [el]])))
