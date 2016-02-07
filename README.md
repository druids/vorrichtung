Vorrichtung
===========

Vorrichtung is a lightweight framework for decorating server-side generated HTML. It's based on
 [Re-frame](https://github.com/Day8/re-frame) and [Reagent](https://github.com/reagent-project/reagent).


[![Build Status](https://travis-ci.org/druids/vorrichtung.svg?branch=master)](https://travis-ci.org/druids/vorrichtung)


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


Development
-----------

* Call `lein dev` to start demo app.


### Other commands

* `lein ancient` - shows outdated dependencies and plugins

Testing
-------

* `lein test`
