(ns vorrichtung.utils
  (:require [clojure.string :as string]))


(defn dash->camel
  [value]
  (let [parts (string/split value "-")]
    (string/join
      (concat
        [(first parts)]
        (map
          string/capitalize
          (rest parts))))))
