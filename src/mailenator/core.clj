(ns mailenator.core
  (:require
    [cheshire.core :as json]
    [clj-http.client :as client]
    [clojure.set :as set]))

(def sandbox
  "0f2ca2f94c2c4fe7ab948da9d4dd4c78")

(def api-key
  "bb20d7933993e9d5351d2eca8325f8d5")

(defn mailgun-params
  "Applies parameter transformations from JSON to Mailgun API."
  [params]
  (-> params
    (assoc :from (str "Mailgun Sandbox <postmaster@sandbox" sandbox ".mailgun.org>"))
    (set/rename-keys {"body" "html"
                      :body :html})))

(defn send-email
  "Sends an email via Mailgun. Returns the request response."
  [params]
  (client/post
    (str "https://api.mailgun.net/v3/sandbox" sandbox ".mailgun.org/messages")
    {:throw-entire-message? true
     :basic-auth ["api" (str "key-" api-key)]
     :form-params (mailgun-params params)}))

(defn send-email-from-json
  "Given a JSON string, sends an email. Returns the request response."
  [s]
  (send-email (json/parse-string s)))
