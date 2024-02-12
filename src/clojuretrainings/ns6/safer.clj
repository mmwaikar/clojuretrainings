(ns clojuretrainings.ns6.safer)

;; maps require no definition of any sort: you just use literal syntax to 
;; build them right on the spot

;; However, a record will be created more quickly, consume less memory, and look
;; up keys in itself more quickly than the equivalent array map or hash map

;; But, we must define the record before using it
(defrecord Person [fname lname])

;; This creates a new Java class with a constructor that takes a value for each 
;; of the fields listed. Hence, notice how a record is created (similar to Java classes)

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

(comment
  (records-ex)
  )