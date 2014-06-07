(ns ring.middleware.defaults
  (:require [ring.middleware.x-headers :as x])
  (:use [ring.middleware.keyword-params :only [wrap-keyword-params]]
        [ring.middleware.nested-params :only [wrap-nested-params]]
        [ring.middleware.params :only [wrap-params]]
        [ring.middleware.cookies :only [wrap-cookies]]
        [ring.middleware.absolute-redirects :only [wrap-absolute-redirects]]))

(def default-options
  {:keyword-params true
   :nested-params  true
   :params  true
   :cookies true
   :absolute-redirects true
   :frame-options :sameorigin
   :content-type-options :nosniff
   :xss-protection {:enable? true :mode :block}})

(defn- wrap [handler middleware options]
  (if (true? options)
    (middleware handler)
    (if options
      (middleware handler options)
      handler)))

(defn- wrap-xss-protection [handler options]
  (x/wrap-xss-protection handler (:enable? options true) (dissoc options :enable?)))

(defn wrap-site-defaults
  [handler & [{:as options}]]
  (let [opts (merge default-options options)]
    (-> handler
        (wrap wrap-keyword-params (:keyword-params opts))
        (wrap wrap-nested-params  (:nested-params opts))
        (wrap wrap-params  (:params opts))
        (wrap wrap-cookies (:cookies opts))
        (wrap wrap-absolute-redirects (:absolute-redirects opts))
        (wrap wrap-xss-protection  (:xss-protection opts))
        (wrap x/wrap-frame-options (:frame-options opts))
        (wrap x/wrap-content-type-options (:content-type-options opts)))))
