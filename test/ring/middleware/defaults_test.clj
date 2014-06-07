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
      (is (= resp (response "foo"))))))
