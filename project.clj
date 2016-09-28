(defproject helda "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://helda.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [org.clojure/core.async "0.2.391"]
    [prismatic/schema "1.1.3"]
    [com.novemberain/monger "3.0.2"]
    ]
  :main ^:skip-aot helda.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
