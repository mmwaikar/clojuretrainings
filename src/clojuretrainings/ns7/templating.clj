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
(def nested-data-template "Hello {{person.fn}} {{person.ln}}!")
(def array-data-template "Hello {{persons.1.fn}} {{persons.1.ln}}!")
(def filter-template "Hello {{fn|capitalize}} {{ln|upper}}, today is {{dt|date:shortDate}}.")

(def email-file-template "email.txt")
(def body-file-template "body.html")
(def index-file-template "index.html")
(def child-a-file-template "child-a.html")
(def child-b-file-template "child-b.html")

(defn selmer-examples []
  (let [name-data {:name "King"}
        nested-data {:person {:fn "Roger" :ln "Federer"}}
        array-data {:persons [{:fn "Roger" :ln "Federer"} {:fn "Novak" :ln "Djokovic"}]}
        ht-output (sp/render hello-template name-data)
        nt-output (sp/render nested-data-template nested-data)
        at-output (sp/render array-data-template array-data)
        et-output (sp/render-file email-file-template name-data)]
    (println "hello-template output:" ht-output)
    (println "nested-data-template output:" nt-output)
    (println "array-data-template output:" at-output)
    (println "email-template output:")
    (println et-output)))

(defn selmer-html-examples []
  (let [countries-data {:countries ["India" "USA"]}
        html-data (merge {:cricketer true :footballer false} countries-data)
        filter-data {:fn "rafael" :ln "nadal" :dt (java.util.Date.)}
        countries-output (sp/render-file body-file-template countries-data)
        index-output (sp/render-file index-file-template html-data)
        ft-output (sp/render filter-template filter-data)
        child-a-output (sp/render-file child-a-file-template {})
        child-b-output (sp/render-file child-b-file-template {})]
    
    ;; Tags
    ;; Tags are used to add various functionality to the template such as 
    ;; looping and conditions. The below example uses a for tag.
    (println "body html output:" countries-output)
    (println "index html output:" index-output)
    
    ;; Filters
    ;; In many cases you may wish to postprocess the value of a variable.
    ;; For example, you might want to convert it to upper case, pluralize it,
    ;; or parse it as a date. This can be done by specifying a filter following
    ;; the name of the variable. The filters are separated using the | character.
    (println "filter-template output:" ft-output)

    ;; Template inheritance
    ;; Templates can inherit from other templates using the extends tag. When
    ;; extending a template, any blocks in the parent will be overwritten by 
    ;; blocks from the child with the same name.
    (println "child-a-output:")
    (println child-a-output)
    (println "child-b-output:")
    (println child-b-output)
    ))

(comment
  (selmer-examples)
  (selmer-html-examples)
  )