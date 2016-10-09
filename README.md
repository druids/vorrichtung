Vorrichtung
===========

Vorrichtung is a lightweight framework for decorating server-side generated HTML. It's based on
 [Re-frame](https://github.com/Day8/re-frame) and [Reagent](https://github.com/reagent-project/reagent).


[![Build Status](https://travis-ci.org/druids/vorrichtung.svg?branch=master)](https://travis-ci.org/druids/vorrichtung)
[![Dependencies Status](https://jarkeeper.com/druids/vorrichtung/status.png)](https://jarkeeper.com/druids/vorrichtung)


Why should you care?
------------------

Because:

1. You have a server-side generated HTML and I want to decorated it. And
2. You think that write front-end with [Re-frame](https://github.com/Day8/re-frame) is a great idea.


Description
-----------

Vorrichtung helps you decorating server-side generated HTML. Typically you have an administration generated with rich
 MVC framework (like Django or Ruby on Rails) and it's needed to enrich the UI. The administration is full
 of auto-completes, modal windows, AJAX/client-side validations etc. With Vorrichtung you creates a component
 ([Reagent](https://github.com/reagent-project/reagent) component) and register it via a CSS selector. When Vorrichtung
 starts iterates over all registered components and try to find matching element. When the element is present
 it renders the component. That's all. Anything after that is
 a normal [Re-frame flow](https://github.com/Day8/re-frame#flow).


Because Vorrichtung only delegates rendering to [Reagent](https://github.com/reagent-project/reagent)
 you can easily reuse your components from SPA. Sure, you can use Vorrichtung with yours old UI components written
 in Javascript (Google Closure Library etc.).


Installation
------------

* Installed [PhantomJS](http://phantomjs.org/) 2.1.1 or newer on `$PATH`.
* [Leiningen](http://leiningen.org/) 2.5.3 or newer.


Simple example
--------------

The most simple component could looks like

```html
<div class="simple-component" id="simple-component"></div>
```

```clojure
(:require [vorrichtung.core :refer [register-component start]])

(defn simple-component-view ; standard Reagent component
  [el args]
  [:h2 "I'm a simple component"])

(register-component ".simple-component" [simple-component-view]) ; register with CSS selector and vector

(start) ; starts Vorrichtung itself
```

`simple-component-view` should accepts two arguments:
* `el` - DOM element matched via given CSS selector
* `args` - optional component arguments passed via `data-*` attributes

Every component has to have ID attribute. The ID should by unique through a whole app. With ID you can distinguish between instances
 of a component. The ID can be used as a prefix of a key in `LocalStorage` etc.


Component with arguments
------------------------

Let's extend an example above with arguments. Because HTML is generally generated via server sometimes is neccessary passed
 arguments into a component. This can be easily done with Vorrichtung, see following example:

```html
<div class="simple-component" id="simple-component" data-foo1="bar" foo2=""></div>
```

```clojure
(:require [vorrichtung.core :refer [register-component start]])

(def component-args-desc
  [{:name :foo1 :required true :type :string}
   {:name :foo2 :required false :type :object}
   {:name :foo3 :required false :type :array}])

(defn component-with-args ; standard Reagent component
  [el args]
  [:h2 "I'm a simple component"])

(register-component ".simple-component" [component-with-args component-args-desc]) ; register with CSS selector and vector

(start) ; starts Vorrichtung itself
```

Vorrichtung checks if a matched DOM element has all neccessary arguments. All arguments are automatically converted into Clojure
 native data types. Arguments could have following types:

* `string` - `string`
* `object` - `hash-map`
* `array` - `vector`
* `int` - `int`

Arguments are automatically parsed, validated and passed as a `hash-map` into a registrated component. The `hash-map` has a following structure:

```clojure
{:foo1 ["bar" true]
 :foo2 [nil false]}
```

First item of a `vector` is a parsed value and second is `bool` (`true` when a value is valid otherwise `false`).


For more details see [demo](src/vorrichtung-demo).


Build-in components (vorrichtung.components)
============================================

Grid
----

There is a grid component that supports:

- ordering
- easy to use custom view components

For more detail see an example in [demo](src/vorrichtung-demo/components/grid)


Controls (vorrichtung.core.controls)
====================================

show-element
------------

Shows `target` element by removing the `hidden-class` (default is `hidden`). When the `hidden-class` is removed
the source element is removed from DOM. The action is fired on `click` event.

```html
<div id="show-element" data-target="show-me">Click to show show-me</div>
<div id="show-me" class="hidden"></div>
```

`hidden-class` can be overridden:

```html
<div id="show-element" data-target="show-me" data-hidden-class="custom-class">Click to show show-me</div>
<div id="show-me" class="custom-class"></div>
```

For more details see [demo](src/vorrichtung-demo).

Development
-----------

* Call `lein dev` to start demo app.


### Other commands

* `lein ancient` - shows outdated dependencies and plugins

Testing
-------

* `lein test`
