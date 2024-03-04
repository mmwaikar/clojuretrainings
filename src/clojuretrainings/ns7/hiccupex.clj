(ns clojuretrainings.ns7.hiccupex
  (:require [hiccup2.core :as h]))

;; Hiccup is a library for representing HTML in Clojure. It uses vectors to
;; represent elements, and maps to represent an element's attributes.
;; The first element of the vector is used as the element name. The second
;; attribute can optionally be a map, in which case it is used to supply the
;; element's attributes. Every other element is considered part of the tag's
;; body. It also provides a CSS-like shortcut for denoting id and class
;; attributes.

(defn simple-ex []
  (let [span (str (h/html [:span {:class "foo"} "bar"]))
        script (str (h/html [:script]))
        p (str (h/html [:p]))
        div (str (h/html [:div#foo.bar.baz "bang"]))]
    (println "span html:" span)
    (println "script html:" script)
    (println "p html:" p)
    (println "div html:" div)))

(defn other-ex []
  (let [html (str (h/html [:div
                           [:button#counter-btn
                            {:class "btn active"
                             :style {:padding 5}
                             :on-click (fn [e] "hello")}
                            "Click me!"]]))
        style-ex (str (h/html [:h4
                               {:style
                                {:padding 50
                                 :border "2px solid red"}}]))]
    (println "button html:" html)
    (println "styling html:" style-ex)))

(comment
  (simple-ex)
  (other-ex))