(ns vorrichtung.html)


(defn safe-html
  ([html el]
   [el {:dangerouslySetInnerHTML {:__html html}}])
  ([html]
   (safe-html html :span)))
