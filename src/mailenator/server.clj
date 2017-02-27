(ns mailenator.server
  "This service is intented for consumption by other well behaved services
  in a completely isolated environment. It is not to be exposed to the internet.
  A public facing mail service has additional requirements beyond authentication
  due to the high potential for abuse of such a service."
  (:require
    [cheshire.core :as json]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :as route]
    [mailenator.consumer :as consumer]
    [mailenator.core :as mailenator]
    [ring.adapter.jetty :as jetty]))


(defroutes handler
  (POST "/send" req (mailenator/send-email-from-json (slurp (:body req))))
  (POST "/queue" req (consumer/publish (json/parse-string (slurp (:body req)))))
  (GET "/status" req {:status 200 :body "Running"})
  (route/not-found "Not found."))

(defn -main [& args]
  (let [stop (consumer/init)]
    (jetty/run-jetty handler {:port 3000})))
