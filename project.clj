(defproject coders-at-work/csk "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.esotericsoftware/kryo "4.0.2"]
                 [com.twitter/chill-java "0.8.0"]
                 ]
  :profiles {:dev {:dependencies [[midje "1.8.3"]
                                  [midje-notifier "0.2.0"]]
                   :plugins [[lein-cloverage "1.0.9"]]
                   }}
  )
