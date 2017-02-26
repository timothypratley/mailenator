(defproject mailenator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.5.1"]
                 [compojure "1.5.2"]
                 [clj-http "3.4.1"]
                 [cheshire "5.7.0"]
                 [stencil "0.5.0"]]
  :plugins [[lein-ring "0.11.0"]]
  :ring {:handler mailenator.server/handler}
  :aliases {"send" ["run" "-m" "mailenator.core/send-email-from-json"]}
  :test-selectors {:default (fn [m] (not (:integration m)))
                   :integration :integration})
