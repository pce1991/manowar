(defproject manowar "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/tools.namespace "0.2.5"]]
  :main ^:skip-aot manowar.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
