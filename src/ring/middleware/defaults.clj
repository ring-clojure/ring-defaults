(ns ring.middleware.defaults
  (:use [ring.middleware.params :only [wrap-params]]
        [ring.middleware.cookies :only [wrap-cookies]]
        [ring.middleware.x-headers :as x]))

(def default-options
  {:params true
   :cookies true
   :frame-options :sameorigin})

(defn- wrap [handler middleware options]
  (if (true? options)
    (middleware handler)
    (if options
      (middleware handler options)
      handler)))

(defn wrap-site-defaults
  [handler & [{:as options}]]
  (let [opts (merge default-options options)]
    (-> handler
        (wrap wrap-params (:params opts))
        (wrap wrap-cookies (:cookies opts))
        (wrap x/wrap-frame-options (:frame-options opts)))))
