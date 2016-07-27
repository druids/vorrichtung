(ns vorrichtung.logging
  (:require
    [re-frame.loggers :refer [console]]))


(defn warn
  [& args]
  (apply console (cons :warn args)))


(defn log
  [& args]
  (apply console (cons :log args)))
