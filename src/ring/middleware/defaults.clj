(ns ring.middleware.defaults
  (:require [ring.middleware.x-headers :as x])
  (:use [ring.middleware.flash :only [wrap-flash]]
        [ring.middleware.session :only [wrap-session]]
        [ring.middleware.session.cookie :only [cookie-store]]
        [ring.middleware.keyword-params :only [wrap-keyword-params]]
        [ring.middleware.nested-params :only [wrap-nested-params]]
        [ring.middleware.multipart-params :only [wrap-multipart-params]]
        [ring.middleware.params :only [wrap-params]]
        [ring.middleware.cookies :only [wrap-cookies]]
        [ring.middleware.absolute-redirects :only [wrap-absolute-redirects]]))

(def default-options
  {:flash   true
   :session {:store (cookie-store), :cookie-attrs {:http-only true}}
   :keyword-params   true
   :nested-params    true
   :multipart-params true
   :params  true
   :cookies true
   :absolute-redirects true
   :xss-protection {:enable? true :mode :block}
   :frame-options  :sameorigin
   :content-type-options :nosniff})

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
        (wrap wrap-flash   (:flash opts))
        (wrap wrap-session (:session opts))
        (wrap wrap-keyword-params   (:keyword-params opts))
        (wrap wrap-nested-params    (:nested-params opts))
        (wrap wrap-multipart-params (:multipart-params opts))
        (wrap wrap-params  (:params opts))
        (wrap wrap-cookies (:cookies opts))
        (wrap wrap-absolute-redirects (:absolute-redirects opts))
        (wrap wrap-xss-protection  (:xss-protection opts))
        (wrap x/wrap-frame-options (:frame-options opts))
        (wrap x/wrap-content-type-options (:content-type-options opts)))))
