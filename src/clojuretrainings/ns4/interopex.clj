(ns clojuretrainings.ns4.interopex
  (:import (java.util Random)
           (java.security MessageDigest)
           (org.xml.sax InputSource)
           (org.xml.sax.helpers DefaultHandler)
           (java.io StringReader)
           (javax.xml.parsers SAXParserFactory)
           (myinterop EnglishGreeting HindiGreeting)
           ))

(def xml-to-parse "<foo><bar>Body of bar</bar></foo>")

;; Part 1 - calling Java from Clojure
(defn use-java-classes []
  (let [rnd (new Random)
        rnd-dot-form (Random.)
        next-int (. rnd nextInt)
        next-int-method-first (.nextInt rnd)
        next-int-dot-form (. rnd-dot-form nextInt)
        next-int-with-args (. rnd nextInt 10)
        pi-val (. Math PI)
        pi-short-form (Math/PI)]
    ;; NOTE: Clojure imports java.lang automatically
    (println (str "value of next-int (by calling an instance method): " next-int))
    (println (str "value of next-int-method-first (by calling an instance method): " next-int-method-first))
    (println (str "value of next-int-dot-form (by calling an instance method): " next-int-dot-form))
    (println (str "value of next-int-with-args (by calling an instance method with args): " next-int-with-args))
    (println (str "value of pi (by calling a static method): " pi-val))
    (println (str "value of pi-short-form (by calling a static method): " pi-short-form))))

(defn fluent-api-ex []
  (let [s (StringBuilder. "palindrome!")
        s-copy (StringBuilder. "palindrome!")]
    ;; we are trying to mimic the Java call - s.delete(2, 3).append("s").reverse();
    (println (str "s: " s))
    ;; NOTE: we can have nested let bindings in a function
    ;; The .. macro reads left to right, like Java, not inside out, like Lisp.
    (let [s-mult-ops (.reverse (.append (.delete s 2 3) "s"))
          s-double-dot (.. s-copy (delete 2 3) (append "s") reverse)]
      (println (str "s-mult-ops: " s-mult-ops))
      (println (str "s-double-dot: " s-double-dot)))))

(defn do-to-macro-ex []
  ;; Sometimes you don’t care about the results of method calls and simply
  ;; want to make several calls on the same object. As the “do” in doto
  ;; suggests, you can use doto to cause side effects in the mutable Java world.
  (doto (System/getProperties)
    (.setProperty "name" "Clojure")
    (.setProperty "favoriteColor" "green")))

;; Part 2 - using Java collections

;; Java arrays are special - they do not implement any interface

(defn use-java-arrays []
  (let [arr (make-array String 5)
        obj-array (to-array ["easier" "array" "creation"])
        int-array (into-array [1 2 3])]
    (aset arr 0 "Painstaking")
    (aset arr 1 "to")
    (aset arr 2 "fill")
    (aset arr 3 "in")
    (aset arr 4 "arrays")
    (println "Java's toString impl for arrays:" arr)
    (println "Converted to seq for helpful output:" (seq arr))
    (println (aget arr 0))
    (println (alength arr))

    (println "obj arr:" obj-array)
    (println "int arr:" int-array)
    (println "using to-array for calling Java methods that take variable arg list:")
    (String/format "Training Week: %s Mileage: %d"
                   (to-array [2 26]))
    
    ;; to transform every element, we use amap
    (println "after transformation:" (seq (amap obj-array idx _ (.toUpperCase (aget obj-array idx)))))

    ;; find the length of the longest string
    (println (areduce obj-array idx ret 0 (max ret (.length (aget obj-array idx)))))))

(defn convenience-functions []
  (let [s-arr ["a" "short" "message"]]
    ;; doesn't work because .toUpperCase() is a Java method
    ;; (map .toUpperCase s-arr)
    (println "using member as function:" (map (memfn toUpperCase) s-arr))
    (println "using anonymous function:" (map #(.toUpperCase %) s-arr))
    (println "using instance?:" (instance? Long 10))
    (println "using instance?:" (instance? String 10))
    (println "using Clojure's format over Java's string format:" (format "%s ran %d miles today" "Jim" 8))
    (println (bean (MessageDigest/getInstance "SHA")))))

;; XML parsing example
(def print-element-handler
  (proxy [DefaultHandler] []
    (startElement
      [uri local qname atts]
      (println (format "Saw element: %s" qname)))))

(defn demo-sax-parse [source handler]
  (.. SAXParserFactory newInstance newSAXParser
      (parse (InputSource. (StringReader. source))
             handler)))

;; Exception Handling - discuss checked exceptions
(defn try-finally-ex []
  (try
    (throw (Exception. "something failed"))
    (finally
      (println "we get to clean up"))))

(defn class-available? [class-name]
  ;; try catch example
  (try
    (Class/forName class-name) true
    (catch ClassNotFoundException _ false)))

;; use Java classes
(defn use-own-java-classes []
  (let [eng-greeting (proxy [EnglishGreeting] [])
        hindi-greeting (proxy [HindiGreeting] [])]
    (println "greetings in English:" (. eng-greeting greet "Bob"))
    (println "greetings in Hindi:" (. hindi-greeting greet "Bob"))))

;; Part 3 - calling Clojure from Java

(comment
 (use-java-classes)
 (fluent-api-ex)
 (do-to-macro-ex)
 (use-java-arrays) 
 (convenience-functions)
 (demo-sax-parse xml-to-parse print-element-handler)
 (try-finally-ex)
 (class-available? "a.b.c")
 (class-available? "java.lang.Long")
 (use-own-java-classes)
  )