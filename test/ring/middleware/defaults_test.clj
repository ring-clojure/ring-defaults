(ns ring.middleware.defaults-test
  (:use clojure.test
        ring.middleware.defaults
        [ring.util.response :only [response]]
        [ring.mock.request :only [request]]))

(deftest test-wrap-defaults
  (testing "api defaults"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults api-defaults))
          resp    (handler (request :get "/"))]
      (is (= resp {:status 200
                   :headers {"Content-Type" "application/octet-stream"}
                   :body "foo"}))))

  (testing "site defaults"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults site-defaults))
          resp    (handler (request :get "/"))]
      (is (= (:status resp) 200))
      (is (= (:body resp) "foo"))
      (is (= (set (keys (:headers resp)))
             #{"X-Frame-Options"
               "X-Content-Type-Options"
               "X-XSS-Protection"
               "Content-Type"
               "Set-Cookie"}))
      (is (= (get-in resp [:headers "X-Frame-Options"]) "SAMEORIGIN"))
      (is (= (get-in resp [:headers "X-Content-Type-Options"]) "nosniff"))
      (is (= (get-in resp [:headers "X-XSS-Protection"]) "1; mode=block"))
      (is (= (get-in resp [:headers "Content-Type"]) "application/octet-stream"))
      (let [set-cookie (first (get-in resp [:headers "Set-Cookie"]))]
        (is (.startsWith set-cookie "ring-session="))
        (is (.contains set-cookie "HttpOnly")))))

  (testing "middleware overrides"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults site-defaults {:security {:frame-options :deny}}))
          resp    (handler (request :get "/"))]
      (is (= (get-in resp [:headers "X-Frame-Options"]) "DENY"))
      (is (= (get-in resp [:headers "X-Content-Type-Options"]) "nosniff"))))

  (testing "disabled middleware"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults site-defaults {:security {:frame-options false}}))
          resp    (handler (request :get "/"))]
      (is (nil? (get-in resp [:headers "X-Frame-Options"])))
      (is (= (get-in resp [:headers "X-Content-Type-Options"]) "nosniff"))))

  (testing "ssl redirect"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults secure-site-defaults))
          resp    (handler (request :get "/foo"))]
      (is (= resp {:status 301
                   :headers {"Location" "https://localhost/foo"}
                   :body ""}))))

  (testing "secure api defaults"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults secure-api-defaults))
          resp    (handler (request :get "https://localhost/foo"))]
      (is (= resp {:status 200
                   :headers {"Content-Type" "application/octet-stream"
                             "Strict-Transport-Security"
                             "max-age=31536000; includeSubDomains"}
                   :body "foo"}))))

  (testing "secure site defaults"
    (let [handler (-> (constantly (response "foo"))
                      (wrap-defaults secure-site-defaults))
          resp    (handler (request :get "https://localhost/"))]
      (is (= (:status resp) 200))
      (is (= (:body resp) "foo"))
      (is (= (set (keys (:headers resp)))
             #{"X-Frame-Options"
               "X-Content-Type-Options"
               "X-XSS-Protection"
               "Strict-Transport-Security"
               "Content-Type"
               "Set-Cookie"}))
      (is (= (get-in resp [:headers "X-Frame-Options"]) "SAMEORIGIN"))
      (is (= (get-in resp [:headers "X-Content-Type-Options"]) "nosniff"))
      (is (= (get-in resp [:headers "X-XSS-Protection"]) "1; mode=block"))
      (is (= (get-in resp [:headers "Strict-Transport-Security"])
             "max-age=31536000; includeSubDomains"))
      (is (= (get-in resp [:headers "Content-Type"]) "application/octet-stream"))
      (let [set-cookie (first (get-in resp [:headers "Set-Cookie"]))]
        (is (.startsWith set-cookie "secure-ring-session="))
        (is (.contains set-cookie "HttpOnly"))
        (is (.contains set-cookie "Secure"))))))
