(ns ring.middleware.defaults-test
  (:use clojure.test
        ring.middleware.defaults
        [ring.util.response :only [response]]
        [ring.mock.request :only [request]]))

(deftest test-wrap-site-defaults
  (testing "smoke test"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-site-defaults))
          resp    (handler (request :get "/"))]
      (is (= resp {:status  200
                   :headers {"X-Frame-Options" "SAMEORIGIN"
                             "X-Content-Type-Options" "nosniff"
                             "X-XSS-Protection" "1; mode=block"}
                   :body    "foo"}))))

  (testing "middleware options"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-site-defaults {:frame-options :deny}))
          resp    (handler (request :get "/"))]
      (is (= resp {:status  200
                   :headers {"X-Frame-Options" "DENY"
                             "X-Content-Type-Options" "nosniff"
                             "X-XSS-Protection" "1; mode=block"}
                   :body    "foo"}))))

  (testing "disabled middleware"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-site-defaults {:frame-options false
                                           :content-type-options false
                                           :xss-protection false}))
          resp    (handler (request :get "/"))]
      (is (= resp (response "foo"))))))
