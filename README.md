# Ring-Defaults

[![Build Status](https://travis-ci.org/ring-clojure/ring-defaults.svg?branch=master)](https://travis-ci.org/ring-clojure/ring-defaults)

Knowing what middleware to add to a Ring application, and in what
order, can be difficult and prone to error.

This library attempts to automate the process, by providing sensible
and secure default configurations of Ring middleware for both websites
and HTTP APIs.

## Installation

Add the following dependency to your `project.clj`:

    [ring/ring-defaults "0.1.5"]

## Basic Usage

The `wrap-defaults` middleware sets up standard Ring middleware based
on a supplied configuration:

```clojure
(require '[ring.middleware.defaults :refer :all])

(def site
  (wrap-defaults handler site-defaults))
```

There are four configurations included with the middleware

- `api-defaults`
- `site-defaults`
- `secure-api-defaults`
- `secure-site-defaults`

The "api" defaults will add support for urlencoded parameters, but not
much else.

The "site" defaults add support for parameters, cookies, sessions,
static resources, file uploads, and a bunch of browser-specific
security headers.

The "secure" defaults force SSL. Unencrypted HTTP URLs are redirected
to the equivlant HTTPS URL, and various headers and flags are sent to
prevent the browser sending sensitive information over insecure
channels.

## Proxies

If your app is sitting behind a load balancer or reverse proxy, as is
often the case in cloud-based deployments, you'll want to set `:proxy`
to `true`:

```clojure
(assoc secure-site-defaults :proxy true)
```

This is particularly important when your site is secured with SSL, as
the SSL redirect middleware will get caught in a redirect loop if it
can't determine the correct URL scheme of the request.

## Customizing

The default configurations are just maps of options, and can be
customized to suit your needs. For example, if you wanted the normal
site defaults, but without session support, you could use:

```clojure
(wrap-defaults handler (assoc site-defaults :session false))
```

The following configuration keys are supported:

- `:cookies` - Set to true to parse cookies from the request.

- `:params` -
  A map of options that describes how to parse parameters from the
  request.
  
  - `:keywordize` -
    Set to true to turn the parameter keys into keywords.
    
  - `:multipart` -
    Set to true to parse urlencoded parameters in the query string and
    the request body, or supply a map of options to pass to the
    standard Ring [multipart-params][1] middleware.

  - `:nested` -
    Set to true to allow nested parameters via the standard Ring
    [nested-params][2] middleware

  - `:urlencoded` -
    Set to true to parse urlencoded parameters in the query string and
    the request body.

- `:proxy` -
  Set to true if the application is running behind a reverse proxy or
  load balancer.

- `:responses` -
  A map of options to augment the responses from your application.

  - `:absolute-redirects` -
    Any redirects to relative URLs will be turned into redirects to
    absolute URLs, to better conform to the HTTP spec.

  - `:content-types` -
    Adds the standard Ring [content-type][3] middleware.

  - `:default-charset` -
    Adds a default charset to any text content-type lacking a charset.

  - `:not-modified-responses` -
    Adds the standard Ring [not-modified][4] middleware.

- `:security` -
  Options for security related behaviors and headers.

  - `:anti-forgery` -
    Set to true to add CSRF protection via the [ring-anti-forgery][5]
    library.

  - `:content-type-options` -
    Prevents attacks based around media-type confusion. See:
    [wrap-content-type-options][6].

  - `:frame-options` -
    Prevents your site from being placed in frames or iframes. See:
    [wrap-frame-options][7].
    
  - `:hsts` -
    If true, enable HTTP Strict Transport Security. See: [wrap-hsts][8].
    
  - `:ssl-redirect` -
    If true, redirect all HTTP requests to the equivalent HTTPS URL. A
    map with an `:ssl-port` option may be set instead, if the HTTPS
    server is on a non-standard port. See: [wrap-ssl-redirect][9].

  - `:xss-protection` -
    Enable the X-XSS-Protection header that tells supporting browsers
    to use heuristics to detect XSS attacks. See: [wrap-xss-protection][10].

- `:session` -
  A map of options for configuring session handling via the Ring
  [session][11] middleware.

  - `:flash` - If set to true, the Ring [flash][12] middleware is added.

  - `:store` - The Ring session store to use for storing sessions.

- `:static`
  A map of options to configure how to find static content.

  - `:files` -
    A string containing a directory on disk to serve files from.
    Usually the `:resources` option below is more useful.
  
  - `:resources` -
    A string containing a classpath prefix. This will serve any
    resources in locations starting with the supplied prefix.


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


## License

Copyright © 2015 James Reeves

Distributed under the MIT License, the same as Ring.
