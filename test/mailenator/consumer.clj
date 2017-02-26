(ns mailenator.consumer
  (:require
    [mailenator.core :as mailenator]
    [langohr.core :as rmq]
    [langohr.channel :as lch]
    [langohr.queue :as lq]
    [langohr.consumers :as lc]))

(comment
  "Mailgun provides a queue which I expect to be highly reliable.
  It is possible that Mailgun might be down/unreachable one day
  and the best thing to do in that circumstance is inform the user.
  It is also possible to put the messages onto a queue for processing,
  and only dequeue them when upon confirmation from Mailgun
  that they have queued the email.
  Errors can be handled by either retrying until successful,
  or moving the message to an 'errors' queue for further diagnosis.
  It would be appropriate to do the former on connectivity errors,
  and the later on validity errors.
  Introducing such a queue means we need to consider doing validation
  before allowing a message onto the queue,
  so that a user will receive some feedback rather than nothing happening.")

(defn message-handler
  [ch meta ^bytes payload]
  (println
    "Sent"
    (mailenator/send-email-from-json (String. payload "UTF-8"))))

(defn -main
  [& args]
  (println "Starting Mailenator... press enter to terminate.")
  (let [conn (rmq/connect)
        ch (lch/open conn)
        qname "mailenator.send"]
    (println "Channel id: " (.getChannelNumber ch))
    (lq/declare ch qname {:exclusive false :auto-delete true})
    (lc/subscribe ch qname message-handler {:auto-ack true})
    (read-line)
    (println "Disconnecting...")
    (rmq/close ch)
    (rmq/close conn)
    (println "Stopped.")))
