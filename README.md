# Vaadin TestBench Support

*Support library that enables the use of Selenium's PageFactory and Page Objects Pattern with Vaadin TestBench 4*


## Motivation

Then new component based Element API for TestBench 4 is great and alleviate the need to write homemade helper functions or library.

However, since Selenium's PageFactory works using dynamic proxies, you'll get a ClassCastException if you replace the WebElement attributes in your Page Objects by a subclasses of TestBenchElement.

This library makes it possible to benefit from the new [TestBench Element API](http://demo.vaadin.com/javadoc/com.vaadin/vaadin-testbench-api/) while using the [Page Objects Pattern] (https://code.google.com/p/selenium/wiki/PageObjects) supported by Selenium's [PageFactory] (https://code.google.com/p/selenium/wiki/PageFactory).

## Usage

Please refer to the [Page Objects Pattern] (https://code.google.com/p/selenium/wiki/PageObjects) and [PageFactory] (https://code.google.com/p/selenium/wiki/PageFactory) documentation for examples.

The library also offers a new `@FindByVaadin` annotation to look up for elements using a Vaadin selector string or attributs to construct an `ElementQuery` base on the field type. This feature in still in development and result could differ from using `ElementQuery` as the `SearchContext` will always be the driver.

## Simple Examples

Using Selenium's `@FindBy` annotation :
```
@FindBy(className = "v-table")
private TableElement table;
```
Using `@FindByVaadin` annotation :
```
@FindByVaadin
private TableElement table;
```
Using `@FindByVaadin` annotation with Vaadin Selector String :
```
@FindByVaadin(vaadinSelector = "//com.vaadin.ui.VerticalLayoutElement/com.vaadin.ui.Table")
private TableElement table;
```
Using `@FindByVaadin` annotation to build an `ElementQuery` :
```
@FindByVaadin(index = 2, caption = "Password")
private TextFieldElement field;
```

## Demo

Basic implementation of the Page Object pattern on a [forked Valo Theme Demo] (https://github.com/mouellet/valo-demo) and use of `TestBenchPageFactory`.