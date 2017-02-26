(ns mailenator.server
  (:require
    [mailenator.core :as mailenator]
    [compojure.core :refer [defroutes POST]]
    [compojure.route :as route]))

(comment
  "It would be irresponsible to create a pass through email sending service.
  This only shows how to send email as an integration guide.")

(defroutes routes
  (POST "/send" req (mailenator/send-email-from-json (slurp (:body req))))
  (route/not-found "Not found."))

(def handler #'routes)
