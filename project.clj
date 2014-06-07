(defproject ring/ring-defaults "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [ring/ring-core "1.3.0"]
                 [ring/ring-headers "0.1.0"]
                 [ring/ring-anti-forgery "1.0.0"]
                 [javax.servlet/servlet-api "2.5"]]
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
