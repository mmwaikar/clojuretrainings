(ns clojuretrainings.ns4.interopex
  (:import (java.util Random)))

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