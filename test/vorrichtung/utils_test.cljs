(ns vorrichtung.utils-test
  (:require [cljs.test :refer-macros [deftest testing are]]
            [vorrichtung.utils :refer [dash->camel]]))


(deftest dash->camel-test
  (testing "should convert to camelCase"
    (are [in out] (= out (dash->camel in))
         "foo-bar" "fooBar"
         "foo" "foo"
         "foo-bar-blah" "fooBarBlah")))
