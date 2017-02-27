(ns mailenator.core
  "Sends email via Mailgun.
  Handle templatization of emails."
  (:require
    [cheshire.core :as json]
    [clj-http.client :as client]
    [clojure.java.io :as io]
    [clojure.set :as set]
    [stencil.loader :as loader]
    [stencil.core :as stencil]
    [stencil.utils :as utils]
    [quoin.map-access :as map]))


(comment
  "These values should be read in from the environment, they should not exist in git.
  However seeing as this is a throwaway project,
  I'll leave them here for convenience and delete the Mailgun account later.")

(def sandbox
  "0f2ca2f94c2c4fe7ab948da9d4dd4c78")

(def api-key
  "bb20d7933993e9d5351d2eca8325f8d5")


(defn find-containing-context
  "This function is used to make template attributes required.
  Thus if a caller forgets a template attribute, they will receive an error."
  [context-stack key]
  (loop [curr-context-stack context-stack]
    (if-let [context-top (peek curr-context-stack)]
      (if (and (associative? context-top)
               (map/contains-named? context-top key))
        context-top
        ;; Didn't have the key, so walk down the stack.
        (recur (next curr-context-stack)))
      ;; Either ran out of context stack or key, in either case, we were
      ;; unsuccessful in finding the key.
      (throw
        (ex-info
          (str "Required attribute not found: '" key "'")
          {:key key
           :context-stack context-stack})))))

(defn render-template
  "Replace the email body when a template param exists.
   A template must be specified as [template-name {arguments}]."
  [params]
  (if-let [[template-name attributes] (get params "template")]
    (let [template (or (loader/load (str template-name ".html"))
                       (throw
                         (ex-info
                           (str "Template not found: '" template-name "'")
                           params)))
          html (with-redefs [utils/find-containing-context find-containing-context]
                 (stencil/render template attributes))]
      (-> params
        (assoc "html" html)
        (dissoc "template")))
    params))

(defn mailgun-params
  "Applies parameter transformations from JSON to Mailgun API."
  [params]
  (-> params
    (assoc :from (str "Mailgun Sandbox <postmaster@sandbox" sandbox ".mailgun.org>"))
    (set/rename-keys {"body" "html"
                      :body :html})
    (render-template)))

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
