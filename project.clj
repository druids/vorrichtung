(defproject vorrichtung "0.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [reagent "0.5.1"]
                 [re-frame "0.6.0"]]

  :min-lein-version "2.5.3"

  :source-paths ["src"]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-2"]
            [lein-doo "0.1.6"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/"]
                        :compiler {:main vorrichtung-demo.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "resources/public/js/compiled/test.js"
                                   :main vorrichtung.runner
                                   :optimizations :none}}

                       {:id "min"
                        :source-paths ["src"]
                        :compiler {:main vorrichtung.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]}
  :aliases {
            "dev" ["do" "clean," "figwheel" "dev"]
            "test" ["do" "clean," "doo" "phantom" "test" "once"]})
