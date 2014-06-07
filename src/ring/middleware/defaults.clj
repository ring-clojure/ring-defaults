(ns ring.middleware.defaults
  (:use [ring.middleware.params :only [wrap-params]]
        [ring.middleware.cookies :only [wrap-cookies]]))

(defn wrap-site-defaults
  [handler]
  (-> handler
      wrap-params
      wrap-cookies))
