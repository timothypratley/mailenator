(ns mailenator.core-test
  (:require
    [clojure.string :as string]
    [clojure.test :refer :all]
    [mailenator.core :as mailenator]))

(deftest ^:integration send-email-integration-test
  (testing "Sending an email"
    (is (= 200
           (:status (mailenator/send-email
                      {:to "timothypratley@gmail.com"
                       :subject "Basic test email"
                       :body "Yup, this was a test email."})))))
  (testing "Sending an email from a json string"
    (is (= 200
           (:status (mailenator/send-email-from-json
                      "{\"to\": \"timothypratley@gmail.com\",
                        \"subject\": \"JSON test email\",
                        \"body\": \"<html><body><h1>Hello World!</h1></body></html>\"}"))))))

(deftest render-template-test
  (testing "Email body replacement"
    (let [params {"template" ["welcome" {"name" "Insect Overlords"}]}]
      (is (not (string/blank? (get (mailenator/render-template params) "html")))))))
