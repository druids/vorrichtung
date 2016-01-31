(ns vorrichtung.dom)


(defn nodelist->seq
    "Converts nodelist to (not lazy) seq."
    [nl]
    (let [result-seq (map #(.item nl %) (range (.-length nl)))]
          (doall result-seq)))


(defn all
  [selector]
  (nodelist->seq (js/document.querySelectorAll selector)))
