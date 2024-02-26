(ns clojuretrainings.ns5.asynchronicity
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

;; Clojure’s core.async library allows you to create multiple independent 
;; processes within a single program. At the heart of core.async is the process,
;; a concurrently running unit of logic that responds to events.
;;
;; You can think of processes as having two rules: 
;; 1) when trying to put a message on a channel or take a message off of it, 
;; wait and do nothing until the put or take succeeds, and 
;; 2) when the put or take succeeds, continue executing
;;
;; Channels
;;
;; Channels are used to communicate between independent processes created by
;; go blocks. Channels communicate messages. You can put messages on a channel
;; and take messages off a channel. Processes wait for the completion of put 
;; and take — these are the events that processes respond to.
;;
;; Here, we create a channel named echo-chan.
(def echo-chan (chan))

(defn put-to-and-take-from-channels []
  ;; Here, we create a new process using go. Everything within the go expression
  ;; — called a go block — runs concurrently on a separate thread. Go blocks 
  ;; run your processes on a thread pool that contains a number of threads equal
  ;; to two plus the number of cores on your machine, which means your program 
  ;; doesn’t have to create a new thread for each process.
  ;;
  ;; In this case, the process (println (<! echo-chan)) expresses “when I take
  ;; a message from echo-chan, print it.” The process is shunted to another
  ;; thread, freeing up the current thread and allowing you to continue.
  ;;
  ;; In the expression (<! echo-chan), <! is the take function. It listens to 
  ;; the channel you give it as an argument, and the process it belongs to waits
  ;; until another process puts a message on the channel. When <! retrieves a
  ;; value, the value is returned and the println expression is executed.
  (go (println (<! echo-chan)))

  ;; The expression (>!! echo-chan "ketchup") puts the string "ketchup" on 
  ;; echo-chan and returns true. When you put a message on a channel, the process
  ;; blocks until another process takes the message. In this case, the process
  ;; didn’t have to wait at all, because there was already a process listening
  ;; to the channel, waiting to take something off of it. 
  (>!! echo-chan "ketchup"))

(defn no-listener-channel []
  ;; DON'T DO THIS, because it will block indefinitely. You’ve created a new 
  ;; channel and put something on it, but there’s no process listening to that
  ;; channel. Processes don’t just wait to receive messages; they also wait for
  ;; the messages they put on a channel to be taken.
  (>!! (chan) "mustard"))

(comment
  (put-to-and-take-from-channels)
  ;; (no-listener-channel)
  )