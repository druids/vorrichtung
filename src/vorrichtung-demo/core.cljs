(ns vorrichtung-demo.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [register-handler path register-sub dispatch dispatch-sync subscribe]]
            [goog.dom :as dom]
            [goog.dom.dataset :as dataset]
            [vorrichtung.core :refer [register-component register-control start]]
            [vorrichtung-demo.config :as config]
            [vorrichtung-demo.controls :refer [simple-control]]
            [vorrichtung-demo.handlers]
            [vorrichtung-demo.subs]
            [vorrichtung-demo.views :refer [simple-component-view]]))


(def simple-component-args-desc
  [{:name :foo1 :required true :type :string}
   {:name :foo2 :required false :type :object}
   {:name :foo3 :required false :type :array}])


(register-component ".simple-component" [simple-component-view simple-component-args-desc])

(register-control ".simple-control" [simple-control])

(when config/debug?
  (println "dev mode"))


(defn ^:export run
  []
  (dispatch-sync [:initialize])
  (start))
