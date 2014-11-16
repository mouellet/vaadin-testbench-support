package com.vaadin.testbench.support.pagefactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import com.vaadin.testbench.support.FindByVaadin;

public class TestBenchElementDecorator extends DefaultFieldDecorator {

    private TestBenchCommandExecutor tbCommandExecutor;

    public TestBenchElementDecorator(ElementLocatorFactory factory,
            TestBenchCommandExecutor tbCommandExecutor) {
        super(factory);
        this.tbCommandExecutor = tbCommandExecutor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }

        if (TestBenchElement.class.isAssignableFrom(field.getType())) {
            return TestBench.createElement(
                    (Class<? extends TestBenchElement>) field.getType(),
                    proxyForLocator(loader, locator), tbCommandExecutor);

        } else if (WebElement.class.isAssignableFrom(field.getType())) {
            return proxyForLocator(loader, locator);

        } else if (List.class.isAssignableFrom(field.getType())) {
            return proxyForListLocator(loader, locator);

        } else {
            return null;
        }
    }

    protected boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        // Type erasure in Java isn't complete. Attempt to discover the generic
        // type of the list.
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return false;
        }

        Type listType = ((ParameterizedType) genericType)
                .getActualTypeArguments()[0];

        if (!WebElement.class.equals(listType)
                && !TestBenchElement.class
                        .isAssignableFrom((Class<?>) listType)) {
            return false;
        }

        if (field.getAnnotation(FindBy.class) == null
                && field.getAnnotation(FindBys.class) == null
                && field.getAnnotation(FindAll.class) == null
                && field.getAnnotation(FindByVaadin.class) == null) {
            return false;
        }

        return true;
    }

}
