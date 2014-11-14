package com.vaadin.testbench.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import com.vaadin.testbench.support.pagefactory.TestBenchElementDecorator;
import com.vaadin.testbench.support.pagefactory.TestBenchElementLocatorFactory;

public class TestBenchPageFactory {

    /**
     * See
     * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.WebDriver, Class<T>)}
     */
    public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy) {
        T page = instantiatePage(driver, pageClassToProxy);
        initElements(driver, page);
        return page;
    }

    /**
     * See
     * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.WebDriver, Object)}
     */
    public static void initElements(WebDriver driver, Object page) {
        final WebDriver driverRef = driver;
        final ElementLocatorFactory factoryRef = new TestBenchElementLocatorFactory(
                driverRef);
        initElements(driverRef, factoryRef, page);
    }

    /**
     * See
     * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.ElementLocatorFactory, Object)}
     */
    public static void initElements(WebDriver driver,
            ElementLocatorFactory factory, Object page) {
        final WebDriver driverRef = driver;
        final ElementLocatorFactory factoryRef = factory;
        final FieldDecorator decoratorRef = new TestBenchElementDecorator(
                factoryRef, (TestBenchCommandExecutor) driverRef);
        initElements(decoratorRef, page);
    }

    /**
     * See
     * {@link org.openqa.selenium.support.PageFactory#initElements(org.openqa.selenium.support.pagefactory.FieldDecorator, Object)}
     */
    public static void initElements(FieldDecorator decorator, Object page) {
        Class<?> proxyIn = page.getClass();
        while (proxyIn != Object.class) {
            proxyFields(decorator, page, proxyIn);
            proxyIn = proxyIn.getSuperclass();
        }
    }

    private static void proxyFields(FieldDecorator decorator, Object page,
            Class<?> proxyIn) {
        Field[] fields = proxyIn.getDeclaredFields();
        for (Field field : fields) {
            Object value = decorator.decorate(page.getClass().getClassLoader(),
                    field);
            if (value != null) {
                try {
                    field.setAccessible(true);
                    field.set(page, value);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static <T> T instantiatePage(WebDriver driver,
            Class<T> pageClassToProxy) {
        try {
            try {
                Constructor<T> constructor = pageClassToProxy
                        .getConstructor(WebDriver.class);
                return constructor.newInstance(driver);
            } catch (NoSuchMethodException e) {
                return pageClassToProxy.newInstance();
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
