(ns vorrichtung.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [vorrichtung.arg-validation-test]
              [vorrichtung.dom-test]
              [vorrichtung.utils-test]))

(doo-tests 'vorrichtung.arg-validation-test
           'vorrichtung.dom-test
           'vorrichtung.utils-test)
