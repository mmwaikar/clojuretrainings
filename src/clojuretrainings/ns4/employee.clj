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
  "greet from - clojuretrainings.ns4.Employee")

;; We've used both constructor signatures to create instances of our generated
;; class. We’ve also called a superclass method getName and the overridden method 
;; greet.
(defn -main []
  (let [e1 (new clojuretrainings.ns4.Employee "Vicky")]
    (println "using getName():" (.getName e1))
    (println "using getAge():" (.getAge e1))
    (println "using greet():" (.greet e1)))
  (let [e2 (new clojuretrainings.ns4.Employee "Rocky" 20)]
    (println "using getName():" (.getName e2))
    (println "using getAge():" (.getAge e2))
    (println "using greet():" (.greet e2))))

(comment
  (-main)
  )