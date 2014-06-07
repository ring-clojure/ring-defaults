(ns ring.middleware.defaults
  (:require [ring.middleware.x-headers :as x])
  (:use [ring.middleware.flash :only [wrap-flash]]
        [ring.middleware.session :only [wrap-session]]
        [ring.middleware.session.cookie :only [cookie-store]]
        [ring.middleware.keyword-params :only [wrap-keyword-params]]
        [ring.middleware.nested-params :only [wrap-nested-params]]
        [ring.middleware.anti-forgery :only [wrap-anti-forgery]]
        [ring.middleware.multipart-params :only [wrap-multipart-params]]
        [ring.middleware.params :only [wrap-params]]
        [ring.middleware.cookies :only [wrap-cookies]]
        [ring.middleware.resource :only [wrap-resource]]
        [ring.middleware.file :only [wrap-file]]
        [ring.middleware.not-modified :only [wrap-not-modified]]
        [ring.middleware.content-type :only [wrap-content-type]]
        [ring.middleware.absolute-redirects :only [wrap-absolute-redirects]]))

(def site-defaults
  "A default configuration for a browser-accessible web site."
  {:params    {:urlencoded true
               :multipart  true
               :nested     true
               :keywordize true}
   :cookies   true
   :session   {:flash true
               :store (cookie-store)
               :cookie-attrs {:http-only true}}
   :security  {:anti-forgery   true
               :xss-protection {:enable? true, :mode :block}
               :frame-options  :sameorigin
               :content-type-options :nosniff}
   :static    {:resources "public"
               :files false}
   :correct   {:not-modified-responses true
               :absolute-redirects     true
               :content-types          true}})

(defn- wrap [handler middleware options]
  (if (true? options)
    (middleware handler)
    (if options
      (middleware handler options)
      handler)))

(defn- wrap-xss-protection [handler options]
  (x/wrap-xss-protection handler (:enable? options true) (dissoc options :enable?)))

(defn- wrap-x-headers [handler options]
  (-> handler
      (wrap wrap-xss-protection         (:xss-protection options false))
      (wrap x/wrap-frame-options        (:frame-options options false))
      (wrap x/wrap-content-type-options (:content-type-options options false))))

(defn wrap-defaults
  [handler defaults & [overrides]]
  (let [cfg (merge-with merge defaults overrides)]
    (-> handler
        (wrap wrap-flash              (get-in cfg [:session :flash] false))
        (wrap wrap-anti-forgery       (get-in cfg [:security :anti-forgery] false))
        (wrap wrap-session            (:session cfg false))
        (wrap wrap-keyword-params     (get-in cfg [:params :keywordize] false))
        (wrap wrap-nested-params      (get-in cfg [:params :nested] false))
        (wrap wrap-multipart-params   (get-in cfg [:params :multipart] false))
        (wrap wrap-params             (get-in cfg [:params :urlencoded] false))
        (wrap wrap-cookies            (get-in cfg [:cookies] false))
        (wrap wrap-absolute-redirects (get-in cfg [:correct :absolute-redirects] false))
        (wrap wrap-resource           (get-in cfg [:static :resources] false))
        (wrap wrap-file               (get-in cfg [:static :files] false))
        (wrap wrap-content-type       (get-in cfg [:correct :content-types] false))
        (wrap wrap-not-modified       (get-in cfg [:correct :not-modified-responses] false))
        (wrap wrap-x-headers          (:security cfg)))))
