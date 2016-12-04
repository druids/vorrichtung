(ns vorrichtung.num
  (:require [cljs.reader :refer [read-string]]))


(defn str->int
  "Parses an integer from a string. Returns `nil` if the value is not an integer."
  [value]
  (when (and (string? value) (re-find #"^-?\d+$" value))
    (read-string value)))


(defn str->float
  "Parses a float from a string. Returns `nil` if the value is not a float."
  [value]
  (when (and (string? value) (re-find #"^-?\d+(\.\d+)?$" value))
    (read-string value)))
