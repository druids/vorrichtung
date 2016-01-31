(ns vorrichtung.arg-validation
  (:require [clojure.string :as string]
            [reagent.core :as reagent]
            [goog.dom.dataset :as dataset]
            [goog.json :as json]
            [vorrichtung.dom :as dom]
            [vorrichtung.utils :refer [dash->camel]]))


(defn validate-string
  [value required]
  (let [value-is-nil? (nil? value)
        string-or-nil (if value-is-nil? value (str value))]
    (if required
      [string-or-nil (not (string/blank? value))]
      [string-or-nil (or (goog.isString value) value-is-nil?)])))


(defn parse-json
  [value validate-func]
  (try
    (let [obj (json/parse value)]
      [obj (validate-func obj)])
    (catch :default e [value false])))


(defn validate-object
  [value required]
  (if (and (not required) (string/blank? value))
    [nil true]
    (parse-json value goog/isObject)))


(defn validate-array
  [value required]
  (if (and (not required) (string/blank? value))
    [nil true]
    (parse-json value goog/isArray)))


(defn extract-arg-value
  [el arg]
  (dataset/get el (dash->camel (name (:name arg)))))


(def validators (reagent/atom {:string validate-string
                               :object validate-object
                               :array validate-array}))


(defn validate-arg-with-validator
  [data-type arg el]
  ((get @validators data-type) (extract-arg-value el arg) (get arg :required false)))


(defn validate-arg
  [parsed-args arg el]
  (let [data-type (:type arg)]
    (assoc parsed-args (:name arg) (if (contains? @validators data-type)
                                     (validate-arg-with-validator data-type arg el)
                                     [(extract-arg-value el arg) true]))))


(defn validate-arg-func
  [el]
  #(validate-arg %1 %2 el))


(defn validate-args
  [args-desc el]
  (reduce (validate-arg-func el) {} args-desc))
