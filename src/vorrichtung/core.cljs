(ns vorrichtung.core
  (:require [clojure.string :as string]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [vorrichtung.arg-validation :refer [validate-args]]
            [vorrichtung.dom :as dom]))


(def components (reagent/atom []))


(defn register-component
  ([selector component]
   (swap! components conj [selector component])))


(defn render-component
  [selector view el validated-args]
  (if (string/blank? (.-id el))
    (re-frame.utils/warn (str "Component with selector '" selector "' has an invalid ID '" (.-id el)"'"))
    (reagent/render
      (view el validated-args)
      el)))


(defn format-invalid-args
  [args-desc parsed-args]
  (reduce-kv (fn [invalid-args arg-name [parsed-value valid?]]
               (if valid?
                 invalid-args
                 (conj invalid-args (str (name arg-name) " -> '" parsed-value "'")))) [] parsed-args))


(defn log-invalid-component-args
  [el args-desc parsed-args]
  (re-frame.utils/warn
    (str "Invalid arguments for component #" (.-id el) ": " (string/join " " (format-invalid-args args-desc parsed-args)))))


(defn try-to-render-component
  [selector component el]
  (let [[view args-desc] (component)
        validated-args (validate-args args-desc el)]
    (if (every? true? (map (fn [[_ [_ valid?]]] valid?) validated-args))
      (render-component selector view el validated-args)
      (log-invalid-component-args el args-desc validated-args))))


(defn start
  []
  (doseq [[selector component args-desc] @components]
    (let [els (dom/all selector)]
      (doseq [el els]
        (try-to-render-component selector component el)))))
