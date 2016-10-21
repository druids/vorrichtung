(ns vorrichtung.runner
    (:require
      [doo.runner :refer-macros [doo-tests]]
      [vorrichtung.arg-validation-test]
      [vorrichtung.components.grid.db-test]
      [vorrichtung.components.grid.middlewares-test]
      [vorrichtung.core-test]
      [vorrichtung.dom-test]
      [vorrichtung.num-test]
      [vorrichtung.utils-test]))


(doo-tests 'vorrichtung.arg-validation-test
           'vorrichtung.components.grid.db-test
           'vorrichtung.components.grid.middlewares-test
           'vorrichtung.core-test
           'vorrichtung.dom-test
           'vorrichtung.num-test
           'vorrichtung.utils-test)
