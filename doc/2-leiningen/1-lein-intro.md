# Leiningen

[Leiningen](https://leiningen.org/) is a tool for automating Clojure projects.

It offers various project-related tasks and can:

- create new projects
- fetch dependencies for your project
- run tests
- run a fully-configured REPL
- compile Java sources (if any)
- run the project (if the project isn’t a library)
- generate a maven-style “pom” file for the project for interop
- compile and package projects for deployment

## Creating new projects

1. Create a new project using:

```
lein new app cljproj
```

2. Add a dependency to an external library (to the dependencies section):

```clojure
:dependencies [[clojure.java-time "1.4.2"]]
```

3. Use the new library in your code

4. Run a repl (for your code):

```
lein repl
```

5. Run your code:

```clojure
(require 'cljproj.core)
(cljproj.core/-main)
```