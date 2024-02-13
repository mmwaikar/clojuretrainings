(ns clojuretrainings.ns6.multidispatch)

;; factory functions to create maps
(defn get-human [name gender]
  {:name name :gender gender})

(defn get-professional [name age gender profession]
  {:name name :age age :gender gender :profession profession})

;; multimethods consist of a dispatching method (defined with the defmulti macro), 
;; and one or more methods (defined with the defmethod macro)

;; We can define a specific behavior per data type just like in OOP
;;
;; Multi method example using single dispatch value 
;; (based on the :gender key of a map)
(defmulti greet-humans :gender)

;; implementation of greet-humans for males
(defmethod greet-humans "Male" [human]
  (str "Hello Mr. " (:name human) " (a " (:gender human) ")"))

;; implementation of greet-humans for females
(defmethod greet-humans "Female" [human]
  (str "Hi Ms. " (:name human) " (a " (:gender human) ")"))

;; Or, we can define a specific behavior for a specific attribute, a combination 
;; of attributes, a combination of different arguments’ values
;;
;; Multi method example using multiple dispatch values (based on a dispatch fn)
;; (based on a vector, composed of the :gender and :profession keys of a map)
(defn get-profession-gender [human]
  [(:gender human) (:profession human)])

(defmulti greet-professionals get-profession-gender)

(defmethod greet-professionals ["Male" "Employee"] [human]
  (str "Hello Mr. " (:name human) " (" (:profession human) ")"))

(defmethod greet-professionals ["Female" "Businesswoman"] [human]
  (str "Hi Ms. " (:name human) " (" (:profession human) ")"))

;; You can think of the dispatching function as Clojure’s equivalent to the part 
;; in an OOP language that determines what method in the class hierarchy of an 
;; object should be executed when executing a method on that (done in runtime, 
;; not compile time) — only here you can control that logic.
(defn multimethods-ex []
  (let [sanjoo (get-human "Sanjoo" "Male")
        mary (get-human "Mary" "Female")
  
        maleEmp (get-professional "Bunty" 35 "Male" "Employee")
        femaleBus (get-professional "Rosy" 30 "Female" "Businesswoman")]
    ;; call multimethods
    (println (greet-humans mary))
    (println (greet-humans sanjoo))
    (println (greet-professionals maleEmp))
    (println (greet-professionals femaleBus))))

(comment
  (multimethods-ex)
  )