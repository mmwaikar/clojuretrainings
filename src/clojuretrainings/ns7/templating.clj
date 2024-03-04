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
;;
;; Why would you embed code in your HTML?
;; The main argument to embed code in your HTML is that your HTML files still
;; mostly look like HTML. That means anyone who is familiar with HTML can modify
;; them. The language for the code embedded in your HTML is usually very basic 
;; and easy to understand. It is done so on purpose, so that your templates don't
;; start to have domain logic in them. Instead, they should have what is known
;; as display logic only. Display logic only deals with how the HTML gets generated.
;;
;; Why would you embed HTML in your code?
;; The main argument to embed HTML in your code is that you have the whole power
;; of your main language to work with. Besides simple loops and conditionals,
;; you may want other ways of manipulating your HTML. In Clojure, many of the
;; HTML libraries that embed HTML in your code do so with data representations
;; of the HTML. For instance, Hiccup uses vectors to represent HTML tags. That
;; means you can easily concat things together.
;;
;; You're going to have to decide which of these two you prefer.
;; ** If you can't choose, default to embedding HTML in your code. **

;; Selmer template engine offer the following building blocks:
;; {{ placeholders }}
;; {{ placeholder|filters }}
;; {% tags %}

(def hello-template "Hello {{name}}!")
(def nested-data-template "Hello {{person.fn}} {{person.ln}}!")
(def array-data-template "Hello {{persons.1.fn}} {{persons.1.ln}}!")
(def filter-template "Hello {{fn|capitalize}} {{ln|upper}}, today is {{dt|date:shortDate}}.")

(def email-file-template "./selmer/email.txt")
(def body-file-template "./selmer/body.html")
(def index-file-template "./selmer/index.html")
(def child-a-file-template "./selmer/child-a.html")
(def child-b-file-template "./selmer/child-b.html")

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
    (println "body html output:")
    (println countries-output)
    (println "index html output:")
    (println index-output)
    
    ;; Filters
    ;; In many cases you may wish to postprocess the value of a variable.
    ;; For example, you might want to convert it to upper case, pluralize it,
    ;; or parse it as a date. This can be done by specifying a filter following
    ;; the name of the variable. The filters are separated using the | character.
    (println "filter-template output:")
    (println ft-output)

    ;; Template inheritance
    ;; Templates can inherit from other templates using the extends tag. When
    ;; extending a template, any blocks in the parent will be overwritten by 
    ;; blocks from the child with the same name.
    (println "child-a-output:")
    (println child-a-output)
    (println "child-b-output:")
    (println child-b-output)))

(comment
  (selmer-examples)
  (selmer-html-examples)
  )