(ns clojuretrainings.ns5.concurrency)

;; State
;; 
;; State is the current set of values associated with things in a program.
;; There’s no problem with state, per se, or even with mutating state. The
;; problem occurs when concurrent (multithreaded) programs share this sort of 
;; state among different threads and then attempt to make updates to it. When 
;; the illusion of single-threaded execution breaks down, the code encounters 
;; all manner of inconsistent data.
;; 
;; Common problems with shared state
;;
;; LOST OR BURIED UPDATES
;; Lost updates occur when two threads update the same data one after the other. 
;; The update made by the first thread is lost because it’s overwritten by the 
;; second one.
;; 
;; DIRTY AND UNREPEATABLE READS
;; A dirty read happens when a thread reads data that another thread is in the 
;; process of updating. When the updating thread is done, the data that was read
;; by the other thread is inconsistent (dirty). Similarly, an unrepeatable read 
;; happens when a thread reads a particular set of data, but because other 
;; threads are updating it, the thread can never do another read that results in
;; it seeing the same data again.
;; 
;; PHANTOM READS
;; A phantom read happens when a thread reads data that’s been deleted (or more 
;; data is added). It's called phantom because the data no longer exists.
;; 
;; The traditional solution
;;
;; The most obvious solution to these problems is to impose a level of control
;; on those parts of the code that mutate shared data. This is done using locks,
;; which are constructs that control the execution of sections of code, ensuring
;; that only a single thread runs a lock-protected section of code at a time.
;; 
;; Disadvantages of Locking
;;
;; Reduced throughput - when one thread is holding the lock, other threads are 
;; waiting
;; The programmer must remember to lock
;; 
;; Other problems with locking
;; 
;; Deadlock - two or more threads are waiting for each other
;; Starvation - a thread is not allocated enough resources, so it starves and
;; never completes
;; Race conditions - interleaving of execution of threads causes an undesired
;; computational result
;;
;; IDENTITY & STATE
;;
;; Let's talk about the favorite set of movies of a person. What changes over 
;; time, therefore, is not the set itself but which set the entity “favorite 
;; movies” refers to. It’s important to realize that we’re talking about two 
;; distinct concepts.
;; 
;; The first is that of an identity — someone’s favorite movies. 
;; The second is the sequence of values that this identity assumes over the 
;; course of the program. These two ideas give us an interesting definition of 
;; state — the value of an identity at a particular point in time.
;; 
;; In a language like Java or Ruby, the minute a class is defined with stateful
;; fields and destructive methods (those that change a part of the object), 
;; concurrency issues begin to creep into the world and can lead to many of the 
;; problems discussed earlier. That's why we need - 
;;
;; Immutable values
;; An immutable object is one that can’t change once it has been created. In
;; order to simulate change, you’d have to create a whole new object and replace
;; the old one.
;;
;; The problem with objects
;;
;; Most OO languages make no distinction between an identity such as favorite-movies 
;; and the memory location where the data relating to that identity is stored.
;; In typical OO languages, when a destructive method (or procedure) executes,
;; it directly alters the contents of the memory where the instance is stored.
;; NOTE: this is not applicable to primitives like numbers or strings.
;;
;; Instead of letting programs have direct access to memory locations via 
;; pointers such as favorite-movies and allowing them to change the content of
;; that memory location, programs should have only a special reference to 
;; immutable objects. The only thing they should be allowed to change is this
;; special reference itself, by making it point to a completely different,
;; suitably constructed object that’s also immutable.
;;
;; Problems with mutation
;;
;; The problems with mutation can be classified into two general types: losing
;; updates (or updating inconsistent data) and reading inconsistent data.
;; If all data is immutable, then we eliminate the second issue.
;; Solving this second problem requires some form of supervision by the language
;; runtime and that's where the special nature of references comes into play.
;; 
;; The Clojure Way
;; 
;; Instead of letting an identity be a simple reference (direct access to a
;; memory location and its contents), it can be a managed reference that points
;; to an immutable value.
;;
;; PERSISTENT DATA STRUCTURES
;; A persistent data structure is one that preserves the previous version of 
;; itself when it’s modified. Older versions of such data structures persist 
;; after updates. Such data structures are inherently immutable, because update
;; operations yield new values every time. All of the core data structures 
;; offered by Clojure are persistent.
;;
;; Coordinated access is used when two Identities needs to change together, the 
;; classic example is moving money from one bank account to another, it needs 
;; to either move completely or not at all.
;;
;; Uncoordinated access is used when only one identity needs to update.
;;
;; Synchronous access is used when the call is expected to wait until all 
;; identities have settled before continuing.
;;
;; Asynchronous access is "fire and forget" and let the identity reach its new
;; state in its own time.
;;
;; Clojure provides managed references to state -
;; ref   Shared changes, synchronous, coordinated changes
;; atom  Shared changes, synchronous, independent (uncoordinated) changes
;; agent Shared changes, asynchronous, independent (uncoordinated) changes
;; var   Isolated changes
;;
;; Software transactional memory
;; Software transactional memory (STM) is a concurrency control mechanism that
;; works in a fashion similar to database transactions. Instead of controlling
;; access to data stored on disks, inside tables and rows, STMs control access 
;; to shared memory.
;;
;; ATOMIC, CONSISTENT, ISOLATED
;; The Clojure STM system has ACI properties (atomicity, consistency, isolation).
;; It doesn’t support durability because it isn’t a persistent system and is
;; based on volatile, in-memory data.
;;
;; Either all the changes happen, or, if the transaction fails, the changes are
;; rolled back and no change happens. This is how the system supports atomicity.
;; If the validator function fails, the transaction is rolled back. In this
;; manner, the STM system supports consistency.
;; When refs are mutated inside a transaction, the changed data are called in -
;; transaction values. This is because they’re visible only to the thread that
;; made the changes inside the transaction. In this manner, transactions isolate
;; the changes within themselves (until they commit).

;; creating
(def a-ref (ref 0))
(def an-atom (atom 0))
(def an-agent (agent 0))

;; Unlike futures, delays, and promises, dereferencing an atom (or any other 
;; reference type) will never block. When you dereference futures, delays, and
;; promises, it’s like you’re saying “I need a value now, and I will wait until
;; I get it,” so it makes sense that the operation would block. However, when
;; you dereference a reference type, it’s like you’re saying “give me the value
;; I’m currently referring to,” so it makes sense that the operation doesn’t 
;; block, because it doesn’t have to wait for anything.

;; reading
(deref a-ref)    ;; or @a-ref
(deref an-atom)  ;; or @an-atom
(deref an-agent) ;; or @an-agent

(def acc1-atom (atom 1000 :validator #(>= % 0)))
(def acc2-atom (atom 1000 :validator #(>= % 0)))
(def acc1-ref (ref 1000 :validator #(>= % 0)))
(def acc2-ref (ref 1000 :validator #(>= % 0)))

;; problems with atoms
(defn transfer-using-atoms [from-acct to-acct amt]
  ;; To update the atom so that it refers to a new state, you use swap!. This 
  ;; might seem contradictory, because we said that atomic values are unchanging. 
  ;; Indeed, they are! But now we’re working with the atom reference type, a
  ;; construct that refers to atomic values. The atomic values don’t change, 
  ;; but the reference type can be updated and assigned a new value.
  (swap! to-acct + amt)
  (swap! from-acct - amt))

(defn transfer-using-refs [from-acct to-acct amt]
  ;; Because refs are meant for situations where multiple threads need to 
  ;; coordinate their changes, the Clojure runtime demands that mutating a ref
  ;; be done inside an STM transaction. STM stands for software transactional 
  ;; memory, and an STM transaction is analogous to a database transaction but
  ;; for changes to in-memory objects.
  ;;
  ;; You start an STM transaction using a built-in macro called dosync. dosync 
  ;; is required for any function that mutates a ref.
  (dosync
   (alter to-acct + amt)
   (alter from-acct - amt)))

(defn atom-problems []
  (dotimes [_ 1000]
    (future (transfer-using-atoms acc2-atom acc1-atom 100)))
  (println "acc1-atom: " @acc1-atom)
  (println "acc2-atom: " @acc2-atom))

(defn no-ref-problems []
  (dotimes [_ 1000]
    (future (transfer-using-refs acc2-ref acc1-ref 100)))
  ;; This works because when the alter to reduce from-acct happens and fails,
  ;; the increment to to-acct also fails.
  (println "acc1-ref: " @acc1-ref)
  (println "acc2-ref: " @acc2-ref))

;; Futures
;; 
;; A future is a simple way to run code on a different thread, and it’s useful 
;; for long-running computations or blocking calls that can benefit from 
;; multithreading.

(defn long-calculation [num1 num2]
  (Thread/sleep 5000)
  (* num1 num2))

(defn long-run []
  (let [x (long-calculation 11 13)
        y (long-calculation 13 17)
        z (long-calculation 17 19)]
    (* x y z)))

(defn fast-run []
  (let [x (future (long-calculation 11 13))
        y (future (long-calculation 13 17))
        z (future (long-calculation 17 19))]
    (* @x @y @z)))

;; Vars can be thought of as pointers to mutable storage locations, which can be
;; updated on a per-thread basis. When a var is created, it can be given an initial
;; value, which is referred to its root binding. Dynamic vars are most often used
;; to name a resource that one or more functions target.

(def ^:dynamic *mysql-host*)

(def mysql-hosts ["test-mysql" "dev-mysql" "staging-mysql"])

(defn db-query [db]
  (binding [*mysql-host* db]
    (count *mysql-host*)))

(defn query-all-dbs []
  (pmap db-query mysql-hosts))

(comment
  (atom-problems)
  (no-ref-problems)
  (time (long-run))
  (time (fast-run))
  (query-all-dbs)
  )