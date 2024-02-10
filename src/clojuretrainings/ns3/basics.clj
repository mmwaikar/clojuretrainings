(ns clojuretrainings.ns3.basics)

(defn boolean-ex []
  (let [t true
        f false
        n nil
        anything-else "hi"
        misc 1]
    (if t 
      (println "t (set to true) is true")
      (println "t (set to true) can never be false"))
    
    (if f
      (println "f (set to false) can never be true")
      (println "f (set to false) is false"))
    
    (if n
      (println "n (set to nil) can never evaluate to true")
      (println "n (set to nil) always evaluates to false"))
    
    (if anything-else
      (println "anything other than false or nil (e.g. a string) eveluates to true")
      (println "any valid value can never be false"))
    
    (if misc
      (println "anything other than false or nil (e.g. in int) eveluates to true")
      (println "any valid value can never be false"))))

;; predicates - a fn which returns true or false
;; idiomatic in Clojure to name predicates with a trailing question mark (?) 
(defn even? [n]
  (= (rem n 2) 0))

(defn even-str [n]
  (if (even? n) "even" "odd"))

(defn predicate-ex []
  (println "26 is:" (even-str 26))
  (println "3 is:" (even-str 3)))

;; multi-arity (i.e. numbr of arguments) function
(defn greeting
  "Returns a greeting of the form 'Hello, username.'
   Default username is 'world'."
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))

;; variable arity function
(defn languages-on-jvm [lang1 lang2 & langs]
  (println lang1 "," lang2 "and" (count langs) "other languages target the JVM"))

(comment
  (boolean-ex)
  (predicate-ex)
  (greeting)
  (greeting "Bob")
  (languages-on-jvm "Clojure" "Scala" "Groovy" "Flix")
  )