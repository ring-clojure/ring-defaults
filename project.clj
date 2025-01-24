(defproject ring/ring-defaults "0.6.0"
  :description "Ring middleware that provides sensible defaults"
  :url "https://github.com/ring-clojure/ring-defaults"
  :license {:name "The MIT License"
            :url  "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring/ring-core "1.13.0"]
                 [ring/ring-ssl "0.4.0"]
                 [ring/ring-headers "0.4.0"]
                 [ring/ring-anti-forgery "1.4.0"]
                 [org.ring-clojure/ring-websocket-middleware "0.2.1"]]
  :aliases
  {"test-all" ["with-profile" "default:+1.10:+1.11:+1.12" "test"]}
  :profiles
  {:dev  {:dependencies [[ring/ring-mock "0.4.0"]]}
   :1.10 {:dependencies [[org.clojure/clojure "1.10.3"]]}
   :1.11 {:dependencies [[org.clojure/clojure "1.11.4"]]}
   :1.12 {:dependencies [[org.clojure/clojure "1.12.0"]]}})
