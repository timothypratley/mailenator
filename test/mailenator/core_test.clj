(ns mailenator.core-test
  (:require [clojure.test :refer :all]
            [mailenator.core :refer :all]))

(deftest ^:integration send-email-integration-test
  (testing "Sending an email"
    (is (= 200
           (:status (send-email
                      {:to "timothypratley@gmail.com"
                       :subject "Basic test email"
                       :body "Yup, this was a test email."})))))
  (testing "Sending an email from a json string"
    (is (= 200
           (:status (send-email-from-json
                      "{\"to\": \"timothypratley@gmail.com\",
                        \"subject\": \"JSON test email\",
                        \"body\": \"<html><body><h1>Hello World!</h1></body></html>\"}"))))))
