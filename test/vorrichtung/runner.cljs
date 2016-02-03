(ns vorrichtung.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [vorrichtung.arg-validation-test]
              [vorrichtung.core-test]
              [vorrichtung.dom-test]
              [vorrichtung.num-test]
              [vorrichtung.utils-test]))

(doo-tests 'vorrichtung.arg-validation-test
           'vorrichtung.core-test
           'vorrichtung.dom-test
           'vorrichtung.num-test
           'vorrichtung.utils-test)
