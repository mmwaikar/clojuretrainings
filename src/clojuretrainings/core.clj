(ns clojuretrainings.core
  (:require [java-time.api :as t])
  (:gen-class))

(defn time-str
  "Returns a string representation of a datetime in the local time zone."
  [instant]
  (t/format
   (t/with-zone (t/formatter "hh:mm a") (t/zone-id))
   instant))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println "The time is:" (time-str (t/instant))))
