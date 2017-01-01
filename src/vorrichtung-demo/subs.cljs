(ns vorrichtung-demo.subs
  (:require
    [re-frame.core :refer [reg-sub]]))

(reg-sub
 :name
 (fn [db _]
   (:name db)))
