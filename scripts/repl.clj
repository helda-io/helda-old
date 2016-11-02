(defn load-common-libs []
  (use '[clojure.java.io :only (reader writer)]
       'clojure.pprint))

(require 'helda.core)
(set! *print-length* 100)
(load-common-libs)
(helda.core/-main)
