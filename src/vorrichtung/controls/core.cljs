(ns vorrichtung.controls.core
  (:require
    [re-frame.loggers :refer [console]]
    [goog.dom :as dom]
    [goog.dom.classes :as classes]
    [goog.events :refer [listen]]
    [goog.events.EventType :refer [CLICK]]
    [vorrichtung.core :refer [get-value]]))


(defn show-element
  [el args]
  "Shows `target` element when `click` event is fired on `el`. The `hidden-class` can be  overridden
   via `data-hidden-class` attribute."
  (let [target (dom/getElement (get-value args :target))
        hidden-class (get-value args :hidden-class "hidden")]
    (if target
      (listen el CLICK #(do
                          (classes/remove target hidden-class)
                          (dom/removeNode el)))
      (console :warn "Missing element #" (args :target)))))


(def show-element-args-desc
  [{:name :target :required true :type :string}
   {:name :hidden-class :required false :type :string}])
