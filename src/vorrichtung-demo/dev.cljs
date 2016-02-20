(ns vorrichtung-demo.dev
  (:require [figwheel.client :as fw]
            [vorrichtung-demo.core :as core]))

(fw/start {:on-jsload core/run
           :websocket-url "ws://localhost:3449/figwheel-ws"})
