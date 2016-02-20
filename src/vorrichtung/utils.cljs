(ns vorrichtung.utils
  (:require
    [clojure.string :refer [join split capitalize blank?]]
    [goog.json :as json]))


(defn dash->camel
  [value]
  (let [parts (split value "-")]
    (join
      (concat
        [(first parts)]
        (map capitalize (rest parts))))))


(defn dump
  "Converts given arguments into native JS objects and write tehm via `console.log`"
  [& args]
  (js/console.log.apply js/console (clj->js (map clj->js args))))


(defn css-class
  [class-seq]
  (join " " (remove blank? class-seq)))


(defn str->keywordize-json
  "Converts a given JSON as `string` into CLJS `seq` or `object` with keywordized keys.
   It may raises an error while parsing from `goog.json.parse`."
  [string]
  (js->clj (json/parse string) :keywordize-keys true))
