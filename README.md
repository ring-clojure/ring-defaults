# Ring-Defaults

Knowing what middleware to add to a Ring application, and in what
order, can be difficult and prone to error.

This library attempts to automate the process, by providing sensible
and secure default configurations of Ring middleware for both websites
and HTTP APIs.

**Note:** Ring-Defaults is currently in development and not yet released.

## Installation

Add the following dependency to your `project.clj`:

    [ring/ring-defaults "0.1.0-SNAPSHOT"]

## Basic Usage

If you're constructing a website:

```clojure
(def site
  (wrap-defaults handler site-defaults)
```

If you're constructing an API:

```clojure
(def api
  (wrap-defaults handler api-defaults)
```

If you're constructing a secure, SSL-only website:

```clojure
(def secure-site
  (wrap-defaults handler secure-site-defaults)
```

If you're constructing a secure, SSL-only API:

```clojure
(def secure-api
  (wrap-defaults handler secure-api-defaults)
```

These defaults are just configuration maps, and can be customized with
the keys described in the next section:

## Configuration

The following configuration keys are supported:

- `:params`
  - `:urlencoded` - if true, parses URLEncoded parameters
  - `:multipart`  - if true, parses multipart parameters
  - `:nested`     - if true, allows for nested parameters
  - `:keywordize` - if true, turns the parameter keys into keywords

## License

Copyright © 2014 James Reeves

Distributed under the MIT License, the same as Ring.
