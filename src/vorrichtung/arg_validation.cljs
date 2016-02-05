(ns vorrichtung.arg-validation
  (:require [clojure.string :as string]
            [cognitect.transit :as transit]
            [reagent.core :as reagent]
            [goog.dom.dataset :as dataset]
            [goog.json :as json]
            [vorrichtung.dom :as dom]
            [vorrichtung.num :refer [str->int]]
            [vorrichtung.utils :refer [dash->camel]]))


(def json-reader (transit/reader :json))


(defn validate-string
  "Validates a given value as a `string`. Returns `string` or `nil` and `true` if the value is valid,
   otherwise `false` as a `vector`."
  [value required]
  (let [value-is-nil? (nil? value)
        string-or-nil (if value-is-nil? value (str value))]
    (if required
      [string-or-nil (not (string/blank? value))]
      [string-or-nil (or (goog.isString value) value-is-nil?)])))


(defn parse-json
  "Parses JSON from `string` and validates via a given `validate-func`.
   Returns a parsed value and `true` if the parsed value is valid,
   otherwise `false` as a `vector`."
  [value validate-func]
  (try
    (let [obj (transit/read json-reader value)
          valid? (validate-func obj)]
      [(if valid? obj value) valid?])
    (catch :default e [value false])))


(defn validate-object
  "Validates a given value as a `map`. Returns `map` or `nil` and `true` if the value is valid,
   otherwise `false` as a `vector`."
  [value required]
  (if (and (not required) (string/blank? value))
    [nil true]
    (parse-json value map?)))


(defn validate-array
  "Validates a given value as a `vector`. Returns `vector` or `nil` and `true` if the value is valid,
   otherwise `false` as a `vector`."
  [value required]
  (if (and (not required) (string/blank? value))
    [nil true]
    (parse-json value vector?)))


(defn validate-int
  "Validates a given value as a `int`. Returns `int` or `nil` and `true` if the value is a integer,
   otherwise `false` as a `vector`."
  [value required]
  (let [parsed (str->int value)]
    (if (and (not required) (string/blank? value))
      [nil true]
      [parsed (integer? parsed)])))


(defn extract-arg-value
  [el arg]
  (dataset/get el (dash->camel (name (:name arg)))))


(def validators (reagent/atom {:string validate-string
                               :object validate-object
                               :array validate-array
                               :int validate-int}))


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
