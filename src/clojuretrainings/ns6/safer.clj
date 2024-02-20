(ns clojuretrainings.ns6.safer)

;; RECORDS
;;
;; maps require no definition of any sort: you just use literal syntax to 
;; build them right on the spot
;;
;; However, a record will be created more quickly, consume less memory, and look
;; up keys in itself more quickly than the equivalent array map or hash map
;;
;; But, we must define the record before using it
(defrecord Person [fname lname])

;; This creates a new Java class with a constructor that takes a value for each 
;; of the fields listed. Hence, notice how a record is created (similar to Java classes)
;;
;; These classes are automatically imported into the same namespace where the 
;; defrecord and deftype declarations occur, but not in any other namespace.

(defn records-ex []
  (let [r (Person. "Roger" "Federer")
        m {:fname "Roger", :lname "Federer"}]
    ;; a record is printed differently than a map
    (println "record:" r)
    (println "map:" m)

    ;; a map and record are not equal even though they have similar values
    (println "a record and a map with same values are:" (if (= r m) "equal" "not equal"))

    ;; key lookup works fine
    (println "value of record field:" (:fname r))
    (println "value of map field:" (:fname m))

    ;; but, a record cannot serve as a function, whereas a map can
    (println "value of map field:" (m :fname))
    ;; (println "value of record field:" (r :fname))

    ;; You can assoc and dissoc any key you want — adding keys that weren’t defined
    ;; in the defrecord works, though they have the performance of a regular map.
    ;;
    ;; However, dissocing a key given in the record works but returns a regular 
    ;; map rather than a record
    (let [r-key-added (assoc r :age 42)
          m-key-added (assoc m :age 42)
          r-key-deleted (dissoc r :fname)
          m-key-deleted (dissoc m :fname)]
      (println "record with a new key added:" r-key-added)
      (println "map with a new key added:" m-key-added)
      (println "record with existing key deleted: (returns a map :0)" r-key-deleted)
      (println "map with deleted key:" m-key-deleted))))

;; PROTOCOLS
;;
;; A protocol in Clojure is simply a set of function signatures, each with at 
;; least one parameter, that are given a collective name. They fulfill a role 
;; somewhat like Java interfaces — a class that claims to implement a particular 
;; protocol should provide specialized implementations of each of the functions 
;; in that protocol.

(defprotocol Vehicle
 (drive [vehicle])
 (honk [vehicle]))

(defrecord Car [wheels automatic])

;; Types (or records) can implement a protocol after any of them has been 
;; defined. What we’ve just done is impossible with Java interfaces or C++ 
;; classes, because the concrete type (such as Car) must name at the time it’s 
;; defined all the interfaces or classes it’s going to implement. Here we went 
;; the other way around — Car was defined before the Vehicle protocol even 
;; existed, but we easily extended Vehicle to each of them. THIS BECOMES SPECIAL
;; WHEN YOU OWN NEITHER OF THEM.

(extend-type Car
  Vehicle
  (drive [vehicle]
    (let [desc (if (true? (:automatic vehicle)) 
                 "an automatic car" 
                 "a car with gears")]
      (println "Drive" desc)))
  
  (honk [vehicle]
    (println "honking" vehicle "horn")))

;; Protocol and interface method implementations can be written directly inside 
;; a defrecord form - this isn’t just more convenient in many cases, but it can 
;; also produce dramatically faster code.
;;
;; Also note that the fields of the object are now available as locals — we use 
;; val instead of (:val t).
(defrecord Bike [wheels electric]
  Vehicle
  (drive [vehicle]
         (let [desc (if (true? electric)
                      "an electric bike"
                      "a fossil fuel bike")]
           (println "Drive" desc)))
  
  (honk [vehicle]
        (println "honking" vehicle "horn")))

(defn protocols-ex []
  (let [a-car (Car. 4 true)
        g-car (Car. 4 false)
        e-bike (Bike. 2 true)
        ff-bike (Bike. 2 false)]
    (drive a-car)
    (honk g-car)
    (drive ff-bike)
    (honk e-bike)))

;; So ultimately, record types are maps and implement everything maps should —
;; seq along with assoc, dissoc, get, and so forth. For the rare case where 
;; you’re building your own data structure instead of just creating application
;; - level record types, Clojure provides a lower-level deftype construct that’s 
;; similar to defrecord but doesn’t implement anything at all. But that also 
;; means that keyword lookups, assoc, dissoc, and so on will remain unimplemented 
;; unless we implement them ourselves. One final note about deftype — it’s the 
;; one mechanism by which Clojure lets you create classes with volatile and 
;; mutable fields.
;;
;; Reification means to bring something into being or to turn something into a 
;; concrete form. The reify macro takes a protocol, which by itself is an 
;; abstract set of methods and creates a concrete instance of an anonymous data 
;; type that implements that protocol.

(defn reify-ex [self-driving]
  (let [self-driving-car {:wheels 4 :self-driving self-driving}]
    (reify Vehicle
      (drive [vehicle]
             (let [desc (if (true? (:self-driving self-driving-car))
                          "a self-driving car"
                          "an old fashioned car")]
               (println "Drive" desc)))
      
      (honk [vehicle]
            (println "honking a self driving car horn")))))

(comment
  (records-ex)
  (protocols-ex)
  (drive (reify-ex true))
  )