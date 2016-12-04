(defproject vorrichtung "0.9.2"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [binaryage/devtools "0.8.3"]
                 [cljs-ajax "0.5.8"]
                 [frankiesardo/linked "1.2.9"]
                 [lein-kibit "0.1.3"]
                 [reagent "0.6.0"]
                 [re-frame "0.8.0"]]

  :min-lein-version "2.5.1"

  :source-paths ["src"]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-figwheel "0.5.8"]
            [lein-doo "0.1.7"]
            [lein-ancient "0.6.10"]
            [lein-kibit "0.1.3"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :repl false}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/"]
                        :compiler {:main vorrichtung-demo.dev
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src/vorrichtung" "test"]
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
            "dev-test" ["do" "clean," "doo" "phantom" "test" "auto"]
            "test" ["do" "clean," "doo" "phantom" "test" "once," "kibit"]})
