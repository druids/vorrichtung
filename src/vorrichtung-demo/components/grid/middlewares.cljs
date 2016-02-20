(ns vorrichtung-demo.components.grid.middlewares
  (:require
    [ajax.core :refer [GET]]
    [re-frame.core :refer [dispatch after]]
    [vorrichtung.components.grid.db :refer [config->header-fields serialize-order compose-config-path]]))


(defn load-data-handler
  [db [_ prev-config]]
  (let [config (get-in db (compose-config-path prev-config) prev-config)]
    (GET (:url config)
          :handler #(dispatch [:grid/data-loaded config %1])
          :error-handler #(js/console.log  %1)
          :headers {:X-Fields (config->header-fields config)
                    :X-Serialization-Format (:serialization_format config)
                    :X-Order (serialize-order (:order config))}
          :format :json
          :response-format :json
          :keywords? true)))


(def load-data (after load-data-handler))
