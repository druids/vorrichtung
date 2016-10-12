(ns vorrichtung-demo.components.grid.middlewares
  (:require
    [ajax.core :refer [GET]]
    [re-frame.core :refer [dispatch after]]
    [vorrichtung.components.grid.db :refer [config->header-fields serialize-order compose-config-path]]
    [vorrichtung.net :refer [xhrio->response-vec]]))


(defn load-data-handler
  [db [_ prev-config]]
  (let [config (get-in db (compose-config-path prev-config) prev-config)]
    (GET (:url config)
          :handler (fn [xhrio]
                     (let [response-vec (xhrio->response-vec xhrio)]
                       (dispatch [:grid/data-loaded config (first response-vec) (second response-vec)])))
          :error-handler #(js/console.log %)
          :headers {:X-Fields (config->header-fields config)
                    :X-Serialization-Format (:serialization_format config)
                    :X-Order (serialize-order (:order config))
                    :X-Base (:base config)
                    :X-Offset (:offset config)}
          :format :json
          :response-format {:read identity :description "raw"}
          :keywords? true)))


(def load-data (after load-data-handler))
