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
  - `:urlencoded` - whether to parse URLEncoded parameters
  - `:multipart`  - whether to parse multipart parameters
    - `:store` - how to store uploaded files
    - `:encoding` - the encoding to use
  - `:nested` - allows for nested parameters
  - `:keywordize` - turns the parameter keys into keywords
- `:cookies` - whether to parse cookies
- `:session`
  - `:flash` - adds a special flash session that only lasts for one request
  - `:store` - the session store to use
  - `:cookie-name` - the cookie name to store the session key in
  - `:cookie-attrs` - a map of any additional cookie attributes
- `:security`
  - `:anti-forgery` - adds CSRF protection to POST, PUT, PATCH and DELETE methods
  - `:xss-protection` - heuristic XSS detection in the browser
    - `:enable?` - whether to enable XSS detection
    - `:mode` - the XSS detection mode (currently only `:block`)
  - `:frame-options` - may be `:deny`, `:sameorigin` or `{:allow-from url}`
  - `:content-type-options` - may be false or `:nosniff`
  - `:ssl-redirect` - redirect HTTP requests to HTTPS
    - `:ssl-port` - the port to use for the HTTPS URL
  - `:hsts` - adds Strict-Transport-Security
    - `:max-age` - the maximum time in seconds the rule applies for
    - `:include-subdomains?` - true if subdomains are included
- `:static`
  - `:resources` - a directory on the classpath to serve static resources from
  - `:files`     - a directory on disk to serve static resources from
- `:responses`
  - `:not-modified-responses` - return 304 Not Modified responses when appropriate
  - `:absolute-redirects` - turn relative redirect URIs into absolute URLs
  - `:content-type` - guess the response content type from the URI file extension

## License

Copyright © 2014 James Reeves

Distributed under the MIT License, the same as Ring.
