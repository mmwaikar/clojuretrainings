(ns clojuretrainings.ns7.templating
  (:require [selmer.parser :as sp]))

;; Template Engines
;;
;; Templating engines render data (as HTML or any other format) through a template. 
;; A template contains some fixed text with variables (or holes, or placeholders). 
;; The template engine replaces the variables in the template file with actual 
;; values, and displays the final output.
;;
;; Uses of Template Engines
;;
;; To generate HTML
;; To generate emails
;; To generate any kind of text [e.g. DDL (Data Definition Language) scripts]
;;
;; Serving HTML (from the server)
;;
;; When you are serving up HTML pages from the backend, you will need a way to
;; easily generate HTML. You need to generate HTML dynamically based on the data
;; that you wish to display. You will need to loop through lists of data and 
;; display different things conditionally. Loops and conditionals? That's code.
;;
;; There are really two choices:
;; - Embed code in your HTML
;; - Embed HTML in your code

(def hello-template "Hello {{name}}!")
(def email-template "email.txt")

;; (sp/set-resource-path! "./templates")

(defn selmer-examples []
  (let [name-data {:name "King"}
        ht-output (sp/render hello-template name-data)
        et-output (sp/render-file email-template name-data)]
    (println "hello-template output:" ht-output)
    (println "email-template output:" et-output)
    ))

(comment
  (selmer-examples)
  )