(ns clojuretrainings.ns4.employee
  (:import (myinterop AbstractEmployee))
  (:gen-class
    :name clojuretrainings.ns4.Employee
    :extends myinterop.AbstractEmployee
    :constructors {[String] [String]
                   [String int] [String int]}
    :init initialize))

(defn -initialize
  ([name]
   (println "name:" name)
   [[name 5] (ref name)])
  ([name age]
   (println "name, age:" name "," age)
   [[name age] (ref age)]))

(defn -greet [this]
  "greet from - myinterop.Employee")

(defn -main []
  (let [e1 (new clojuretrainings.ns4.Employee "Vicky")]
    (println (.getName e1))
    (println (.getAge e1))
    (println (.greet e1)))
  (let [e2 (new clojuretrainings.ns4.Employee "Rocky" 20)]
    (println (.getName e2))
    (println (.getAge e2))
    (println (.greet e2))))

(comment
  (-main)
  )