(ns vorrichtung-demo.components.grid.middlewares
  (:require
    [ajax.core :refer [GET]]
    [re-frame.core :refer [dispatch after]]
    [vorrichtung.components.grid.db :refer [config->header-fields serialize-order compose-config-path
                                            dispatch-data-loaded-handler]]))


(defn load-data-handler
  [db [_ prev-config]]
  (let [config (get-in db (compose-config-path prev-config) prev-config)]
    (GET (:url config)
          :handler (dispatch-data-loaded-handler :grid/data-loaded config)
          :error-handler #(js/console.log %)
          :headers {:X-Fields (config->header-fields config)
                    :X-Serialization-Format (:serialization_format config)
                    :X-Order (serialize-order (:order config))
                    :X-Base (:base config)
                    :X-Offset (:offset config)}
          :format :json
          :response-format {:read identity :description "raw"} ; it's necessary to use `raw` response,
          :keywords? true)))                                   ; because response's headers need to be processed


(def load-data (after load-data-handler))
