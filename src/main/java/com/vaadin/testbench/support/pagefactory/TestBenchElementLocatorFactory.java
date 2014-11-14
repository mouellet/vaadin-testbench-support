package com.vaadin.testbench.support.pagefactory;

import java.lang.reflect.Field;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.vaadin.testbench.support.FindByVaadin;

public class TestBenchElementLocatorFactory implements ElementLocatorFactory {

    private final SearchContext searchContext;

    public TestBenchElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        if (field.getAnnotation(FindByVaadin.class) != null) {
            return new TestBenchElementLocator(searchContext, field);
        } else {
            return new DefaultElementLocator(searchContext, field);
        }
    }

}
