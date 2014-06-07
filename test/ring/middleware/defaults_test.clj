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
                   :headers {"X-Frame-Options" "SAMEORIGIN"}
                   :body    "foo"}))))

  (testing "middleware options"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-site-defaults {:frame-options :deny}))
          resp    (handler (request :get "/"))]
      (is (= resp {:status  200
                   :headers {"X-Frame-Options" "DENY"}
                   :body    "foo"}))))

  (testing "disabled middleware"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-site-defaults {:frame-options false}))
          resp    (handler (request :get "/"))]
      (is (= resp (response "foo"))))))
