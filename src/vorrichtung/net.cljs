(ns vorrichtung.net
  (:require [reagent.core :as reagent]
            [goog.net.Cookies]))


(def auth-token (reagent/atom nil))
(def csrf-token (reagent/atom nil))


(defn load-auth-token
  []
  (let [cookies (goog.net.Cookies. js/document)]
    (reset! auth-token (.get cookies "Authorization"))
    (reset! csrf-token (.get cookies "csrftoken"))))


(defn default-headers
  ([headers]
   (merge (when-not (nil? @auth-token)
            {:Authorization @auth-token})
          {:X-CsrfToken @csrf-token}
          headers))
  ([]
   (default-headers {})))


(defn xhrio->response-vec
  "Takes a pure `goog.net.XhrIO` objects and returns it's response as a vector with a response and headers.
   Both items are CLJS maps."
  [xhrio]
  [(-> xhrio .getResponseJson (js->clj :keywordize-keys true))
   (-> xhrio .getResponseHeaders (js->clj :keywordize-keys true))])
