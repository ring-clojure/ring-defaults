# ring-defaults

[![Build Status](https://travis-ci.org/ring-clojure/ring-defaults.svg?branch=master)](https://travis-ci.org/ring-clojure/ring-defaults)

Choosing and ordering middleware for a Ring application can be difficult and error-prone.

This library attempts to automate the process by providing sensible and secure default configurations of Ring middleware for both websites and HTTP APIs.

## Installation

### Leiningen

Add the following dependency to your `project.clj`:

```clojure
[ring/ring-defaults "0.3.2"]
```

### Clojure Deps

Merge the following into your `deps.edn`:

```clojure
{:deps {ring/ring-defaults {:mvn/version "0.3.2"}}}
```

## Basic usage

The `wrap-defaults` middleware sets up standard Ring middleware based on a supplied configuration:

```clojure
(require '[ring.middleware.defaults :refer :all])

(def site
  (wrap-defaults handler site-defaults))
```

There are four included configurations:

* `api-defaults`
* `site-defaults`
* `secure-api-defaults`
* `secure-site-defaults`

The `api` defaults add support for URL-encoded parameters, but not much else.

The `site` defaults add support for parameters, cookies, sessions, static resources, file uploads, and a bunch of browser-specific security headers.

The `secure` defaults force SSL; redirect unencrypted HTTP requests to the equivalent HTTPS URL; send various headers and flags to prevent the browser from sending sensitive information over insecure channels.

## Proxies

Set `:proxy` to `true` if your application sits behind a load balancer or other reverse proxy, as is often the case in cloud-based deployments:
```clojure
(assoc secure-site-defaults :proxy true)
```

This is particularly important for sites secured with SSL: the SSL redirection middleware will get caught in a redirect loop if it can’t determine the request’s correct URL scheme.

## Customizing

The default configurations are just maps of options, and can be customized to suit your needs. For example, if you wanted the normal site defaults, but without session support, you could use:
```clojure
(wrap-defaults handler (assoc site-defaults :session false))
```

Supported configuration keys are:

* `:cookies` – set to `true` to parse cookies from the request.

* `:params` – a map of options that describes how to parse parameters from the
  request:
  
  * `:keywordize` – set to `true` to turn the parameter keys into keywords.
    
  * `:multipart` – set to `true` to parse URL-encoded parameters, in both the query string and request body; or supply a map of options to pass to [the standard Ring multipart-params middleware][1].

  * `:nested` – set to `true` to allow nested parameters, via [the standard Ring nested-params middleware][2].

  * `:urlencoded` – set to `true` to parse URL-encoded parameters, in both the query string and request body.

* `:proxy` – set to `true` if the application runs behind a reverse proxy or load balancer.

* `:responses` – a map of options to augment the application’s responses:

  * `:absolute-redirects` – set to `true` to replace any relative URLs in redirects with     absolute URLs, to better conform to the HTTP specification.

  * `:content-types` – set to `true` to add [the standard Ring content-type middleware][3].

  * `:default-charset` – set to `true` to add a default `charset` to `Content-Type: text/*` headers that lack a `charset`.

  * `:not-modified-responses` – set to `true` to add [the standard Ring not-modified middleware][4].

* `:security` – a map of options for security-related behaviors and headers:

  * `:anti-forgery` – set to `true` to add CSRF protection, via [the ring-anti-forgery
    library][5].

  * `:content-type-options` – set to `true` to prevent attacks based around `Content-Type` confusion. See [wrap-content-type-options][6].

  * `:frame-options` – set to `true` to prevent the application from being placed in frames or iframes. See [wrap-frame-options][7].
    
  * `:hsts` – set to `true` to enable HTTP Strict Transport Security. See [wrap-hsts][8].
    
  * `:ssl-redirect` – set to `true` to redirect all HTTP requests to the equivalent HTTPS URL; or set to a map with an `:ssl-port` option for the same functionality where the HTTPS server is on a non-standard port:
    ```clojure
    {:ssl-redirect true}
    {:ssl-redirect {:ssl-port "1234"}}
    ```
    
    See [wrap-ssl-redirect][9].

  * `:xss-protection` – set to `true` to enable the `X-XSS-Protection` header that tells supporting browsers to use heuristics to detect XSS attacks. See [wrap-xss-protection][10].

* `:session` – a map of options for handling sessions, via [the standard Ring session middleware][11]:

  * `:flash` – set to `true` to add [the Ring flash middleware][12].

  * `:store` – set to an implementation of [the Ring session middleware’s `SessionStore` protocol][13] to override the default [memory session store][14]:
    ```clojure
    {:store ring.middleware.session.cookie/cookie-store}
    {:store myapplication.session/my-session-store}
    ```

* `:static` – a map of options to configure how to find static content:

  * `:files` – set to a `String` containing a directory path, or a collection of at least one of the same, to serve files found in the location(s) given:
    ```clojure
    {:files "/mnt/static"}
    {:files #{"/mnt/static" "/mnt/static-supplement"}}
    ```
    
    The `:resources` option is usually more useful.
  
  * `:resources` – set to a `String` containing a classpath prefix, or a collection of at least one of the same, to serve resources in locations starting with the prefix(es) given:
    ```clojure
    {:resources "static"}
    {:resources #{"static" "static-supplement"}}
    ```

[1]: https://ring-clojure.github.io/ring/ring.middleware.multipart-params.html
[2]: https://ring-clojure.github.io/ring/ring.middleware.nested-params.html
[3]: https://ring-clojure.github.io/ring/ring.middleware.content-type.html
[4]: https://ring-clojure.github.io/ring/ring.middleware.not-modified.html
[5]: https://github.com/ring-clojure/ring-anti-forgery
[6]: https://ring-clojure.github.io/ring-headers/ring.middleware.x-headers.html#var-wrap-content-type-options
[7]: https://ring-clojure.github.io/ring-headers/ring.middleware.x-headers.html#var-wrap-frame-options
[8]: https://ring-clojure.github.io/ring-ssl/ring.middleware.ssl.html#var-wrap-hsts
[9]: https://ring-clojure.github.io/ring-ssl/ring.middleware.ssl.html#var-wrap-ssl-redirect
[10]: https://ring-clojure.github.io/ring-headers/ring.middleware.x-headers.html#var-wrap-xss-protection
[11]: https://ring-clojure.github.io/ring/ring.middleware.session.html
[12]: https://ring-clojure.github.io/ring/ring.middleware.flash.html
[13]: https://ring-clojure.github.io/ring/ring.middleware.session.store.html#var-SessionStore
[14]: https://ring-clojure.github.io/ring/ring.middleware.session.memory.html#var-memory-store


## License

Copyright © 2018 James Reeves

Distributed under the MIT License, the same as Ring.
