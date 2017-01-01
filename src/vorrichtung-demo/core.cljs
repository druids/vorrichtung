(ns vorrichtung-demo.core
  (:require
    [reagent.core :as reagent]
    [re-frame.core :refer [dispatch dispatch-sync]]
    [goog.dom :as dom]
    [goog.dom.dataset :as dataset]
    [devtools.core :as devtools]
    [vorrichtung.core :refer [register-component register-control start]]
    [vorrichtung.controls.core :refer [show-element show-element-args-desc]]
    [vorrichtung-demo.components.grid.core]
    [vorrichtung-demo.config :as config]
    [vorrichtung-demo.controls :refer [simple-control]]
    [vorrichtung-demo.handlers]
    [vorrichtung-demo.subs]
    [vorrichtung-demo.views :refer [simple-component-view]]))


(def simple-component-args-desc
  [{:name :foo1 :required true :type :string}
   {:name :foo2 :required false :type :object}
   {:name :foo3 :required false :type :array}])


(register-component ".simple-component"
                    [(fn [el args]
                       [:button {:on-click #(dispatch [:grid-load "url jak cyp" "grid"])} "click"])
                     simple-component-args-desc])

(register-control ".simple-control" [simple-control])

(register-control ".show-element" [show-element show-element-args-desc])


(when config/debug?
  (enable-console-print!)
  (println "dev mode")
  (devtools/install! [:formatters :hints :async]))


(defn ^:export run
  []
  (dispatch-sync [:initialize])
  (start))
