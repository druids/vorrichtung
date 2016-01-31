(ns vorrichtung-demo.handlers
  (:require [re-frame.core :as re-frame]
            [vorrichtung-demo.db :as db]))

(re-frame/register-handler
 :initialize
 (fn  [_ _]
   db/default-db))
