(ns mailenator.consumer
  "Mailgun provides a queue which I expect to be highly reliable.
  It is possible that Mailgun might be down/unreachable one day.
  It is also possible to put the messages onto a queue for processing,
  and only dequeue them when upon confirmation from Mailgun
  that they have queued the email.
  Errors could be handled by either retrying until successful,
  or moving the message to an 'errors' queue for further diagnosis.
  It would be appropriate to do the former on connectivity errors,
  and the later on validity errors.
  Introducing such a queue means we need to consider doing validation
  before allowing a message onto the queue, so that the problem is
  more easily identified at it's source.
  Hence a new endpoint 'publish' is exposed for this purpose."
  (:require
    [cheshire.core :as json]
    [langohr.basic :as lb]
    [langohr.channel :as lch]
    [langohr.consumers :as lc]
    [langohr.core :as rmq]
    [langohr.queue :as lq]
    [mailenator.core :as mailenator]))


(def qname "mailenator.send")

(def channel nil)


(defn message-handler
  [ch meta ^bytes payload]
  (println
    "Sent"
    (mailenator/send-email-from-json (String. payload "UTF-8"))))

(defn publish [params]
  (let [message (json/generate-string (mailenator/render-template params))]
    (lb/publish
      (or channel (throw (ex-info "Message queue is not open yet" {})))
      ""
      qname
      message
      {:content-type "text/plain"
       :type "email"})
    {:status 200
     :body "Enqueued"}))

(defn init []
  (println "Starting Mailenator... press enter to terminate.")
  (try
    (let [conn (rmq/connect)
          ch (lch/open conn)]
      (alter-var-root #'channel (constantly ch))
      (println "Channel id: " (.getChannelNumber ch))
      (lq/declare ch qname {:exclusive false :auto-delete true})
      (lc/subscribe ch qname message-handler {:auto-ack true})
      (fn stop []
        (println "Disconnecting...")
        (rmq/close ch)
        (rmq/close conn)
        (println "Stopped.")))
    (catch Exception ex
      ;; It would be better to keep retrying,
      ;; but as I expect you haven't installed RabbitMQ yet,
      ;; that would just be annyoingly spammy...
      (println "RabbitMQ connection could not be made, is it running?"))))

(defn -main [& args]
  (let [stop (init)]
    (doseq [message args]
      (publish (json/parse-string message)))
    (read-line)
    (stop)))
