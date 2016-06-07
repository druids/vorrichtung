(ns vorrichtung.num
  (:require [cljs.reader :refer [read-string]]))


(defn str->int
  "Parses an integer from a string. Returns `nil` if the value is not a number."
  [value]
  (when (and (string? value) (re-find #"^-?\d+$" value))
    (read-string value)))
