# Lisp

Before we explore Clojure, it is necessary to understand the Lisp syntax. There aren't languages that can boast of a smaller syntax rules than Lisp.

## Lisp syntax

### Everything is a list

### The first thing coming after open parenthesis `(` is the *name* of the function (or the call to the function), and rest all are *arguments* to that function.

### It uses *prefix* notation

| Notation | Example |
|----------|---------|
| Infix (operator **between** the operands) | 2 + 2 |
| Prefix (operator **before** the operands) | + 2 2 |
| Postfix (operator **after** the operands) | 2 2 + |

#### Advantages of a prefix notation

Q. Can you guess the advantages?

A. Hint: there are two.
1. Single operator for mutiple operands - `(+ 1 2 3 4 5 6)`
2. No need to remember operator precedence (BODMAS rules) - `(* (+ 2 2) 5)`. What's the value of - `2 + 1 * 3 / 4`?

### Example Lisp code

```common-lisp
(select (:title :author :year)
  (from :books)
  (where (:and (:>= :year 1995)
               (:< :year 2010)))
  (order-by (:desc :year)))
```
