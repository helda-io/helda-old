(defn load-common-libs []
  (use '[clojure.java.io :only (reader writer)]
       'clojure.pprint
       'helda.assembly.repl))

(set! *print-length* 100)
(load-common-libs)
