# Vaadin TestBench Support

*Support library that enables the use of Selenium's PageFactory and Page Objects Pattern with Vaadin TestBench 4*


## Motivation

Then new component based `Element API` for TestBench 4 is great and alleviate the need to write homemade helper functions or library.

However, since Selenium's `PageFactory` works using dynamic proxies, you'll get a `ClassCastException` if you replace the WebElement attributes in your Page Objects by a subclasses of `TestBenchElement`.

This library makes it possible to benefit from the new [TestBench Element API](http://demo.vaadin.com/javadoc/com.vaadin/vaadin-testbench-api/) while using the [Page Objects Pattern] (https://code.google.com/p/selenium/wiki/PageObjects) supported by Selenium's [PageFactory] (https://code.google.com/p/selenium/wiki/PageFactory).
