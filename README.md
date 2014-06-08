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
the keys described in the next section.

## Customizing

The default configurations are just maps of options, and can be
customized to suit your needs.

The following configuration keys are supported:

### :proxy

Set to true if the application is running behind a reverse proxy, like
nginx, or behind a load balancer, like ELB.

### :params

A map that contains options to parse parameters from the request.
Parsed parameters are placed in the `:params` key of the request map.

##### :urlencoded

Set to true to parse urlencoded parameters in the query string and the
request body.

##### :multipart

Set to true to parse multipart parameters in the request body. This
allows for file uploads from the browser.

A map of multipart options may also be specified:

- `:store`    - a multipart store that determines where to put uploaded files
- `:encoding` - the character encoding to use for parameters

##### :nested

Set to true to allow nested parameters via a special syntax. For
instance, an encoded parameter string like:

    user[id]=001&user[name]=alice

Will be converted into a map like:

```clojure
{:user {:id "001", :name "alice"}}
```

##### :keywordize

By default the keys in the parameter map are strings. Set this to true
to turn them into keywords.


### :cookies

Set to true to parse cookies from the request. Parsed cookies are
placed in the `:cookies` key on the request map.


### :session

A map that contains options for specifying the session. Sessions are
placed on the `:session` key on the request map, and can be updated
via the `:session` key on the response map.

##### :flash

If set to true, an additional `:flash` key is added to the request and
response maps. This acts like the session, but each item lasts only
until the next request.


##### :store

The session store to use. This structure determines where to keep the
session data.

##### :cookie-name

The name of the cookie that holds the session key. Defaults to "ring-session".

##### :cookie-attrs

A map of additional attributes, such as `:http-only` or `:secure`, to
add to the session cookie.


### :security

A map of security related behaviors and headers.

##### :anti-forgery

Set to true to turn on CSRF protection. All forms must contain an
anti-forgery token. See [ring-anti-forgery] for more details.

[ring-anti-forgery]: https://github.com/ring-clojure/ring-anti-forgery

##### :xss-protection

Enable the X-XSS-Protection header that tells supporting browsers to use
heuristics to detect XSS attacks.

##### :frame-options

Prevent your site from being placed in frames. Accepts the following
values:

- `:deny`
- `:sameorigin`
- `{:allow-from url}`

##### :content-type-options

Prevent attacks based around media type confusion. Can only be set to
`false` or `:nosniff`.

##### :ssl-redirect

If true, redirect all HTTP requests to the equivalent HTTPS URL.

A map with an `:ssl-port` option may be set instead, if the HTTPS
server is on a nonstandard port.

##### :hsts

If true, enable HTTP Strict Transport Security. This prevents your
site from being accessed over HTTP for a set length of time.

A map may also be supplied with the following options:

- `:max-age` - the maximum time in seconds the rule applies for
- `:include-subdomains?` - true if subdomains are included


### :static

A map of options to configure how to find static content.

##### :resources

A string containing a classpath prefix. This will serve any resources
in locations starting with the supplied prefix.


##### :files

A string containing a directory on disk to serve files from. Usually
the `:resources` option mentioned above is more useful.


### :responses

A map of options to augment the responses from your application.

##### :not-modified-responses

If your responses have a `Last-Modified` or `ETag` header, and the
requesting browser has the response already cached, a 304 Not Modified
response will be returned instead.

##### :absolute-redirects

Any redirects to relative URLs will be turned into redirects to
absolute URLs, to better conform to the HTTP spec.

##### :content-type

If true, any response without a content-type will have one added,
based on the file extension of the request URL.


## License

Copyright © 2014 James Reeves

Distributed under the MIT License, the same as Ring.
