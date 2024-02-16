(ns clojuretrainings.ns4.cljfromjava)

(defn greet [name]
  (let [greeting (str "Hello, " name)]
    (println greeting)
    greeting))

;; RT represents the Clojure runtime. The RT class also has a var static method,
;; which accepts a namespace name along with a var name (both as strings). It 
;; looks up a var as specified and returns a Var object, which can then be invoked 
;; using the invoke method. The invoke method can accept an arbitrary number of 
;; arguments.
;; 
;; An example Java class utilizing the above Clojure code.
;; 
;; public class CljCaller {
;;     public static void main(String[] args) throws Exception {
;;         RT.loadResourceScript("cljfromjava.clj");
;;         Var report = RT.var("clojuretrainings.ns4.cljfromjava", "greet");
;;         String result = (String) report.invoke("Bunty");
;;         System.out.println("Result: " + result);
;;     }
;; }
