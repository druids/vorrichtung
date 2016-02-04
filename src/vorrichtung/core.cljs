(ns vorrichtung.core
  (:require [clojure.string :as string]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [vorrichtung.arg-validation :refer [validate-args]]
            [vorrichtung.dom :as dom]))


(def components (reagent/atom []))
(def controls (reagent/atom []))


(defn register-component
  "Registrates a given component that should be a `vector` with render function (Reagent)
   and optional arguments descriptions."
  ([selector component]
   (swap! components conj [selector component])))


(defn register-control
  "Registrates a given control that should be a `vector` with function
   and optional arguments descriptions."
  ([selector control]
   (swap! controls conj [selector control])))


(defn render-component
  "Renders a given component via Reagent/render."
  [selector view el validated-args]
  (when (string/blank? (.-id el))
    (re-frame.utils/warn
      (str "Component with selector '" selector "' has no ID ")
      el))
  (reagent/render
    (view el validated-args)
    el))


(defn format-invalid-args
  "Format invalid arguments for printing."
  [args-desc parsed-args]
  (reduce-kv (fn [invalid-args arg-name [parsed-value valid?]]
               (if valid?
                 invalid-args
                 (conj invalid-args (str (name arg-name) " -> '" parsed-value "'"))))
             []
             parsed-args))


(defn log-invalid-control-args
  [control-type el args-desc validated-args]
  (re-frame.utils/warn
    (str "Invalid arguments for " control-type " #" (.-id el) ": " (string/join
                                                              " "
                                                              (format-invalid-args args-desc validated-args))))
  nil)


(defn all-args-are-valid?
  [validated-args]
  (every? true? (map (fn [[_ [_ valid?]]] valid?) validated-args)))


(defn call-control
  "Call a given control."
  [selector control el validated-args]
  (control el validated-args))


(defn process-control
  "Process a given control/component if all arguments are valid,
   otherwise logs invalid arguments."
  [caller control-type selector [control args-desc] el]
  (let [validated-args (validate-args args-desc el)]
    (if (all-args-are-valid? validated-args)
      (caller selector control el validated-args)
      (log-invalid-control-args control-type el args-desc validated-args))))


(defn start
  "Main function, should be call when app starts. Iterates over all controls/components and process them."
  []
  (doseq [[caller control-type ctrls] [[call-control "control" controls]
                                       [render-component "component" components]]]
    (doseq [[selector control args-desc] (deref ctrls)]
      (let [els (dom/all selector)]
        (doseq [el els]
          (process-control caller control-type selector control el))))))
