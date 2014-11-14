package com.vaadin.testbench.support.pagefactory;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elementsbase.AbstractElement;
import com.vaadin.testbench.support.FindByVaadin;

@SuppressWarnings("unchecked")
public class TestBenchElementLocator implements ElementLocator {

    protected final SearchContext searchContext;
    protected final Field field;
    protected final boolean shouldCache;
    protected final By by;
    protected ElementQuery<?> elementQuery;
    protected WebElement cachedElement;
    protected List<WebElement> cachedElementList;

    public TestBenchElementLocator(SearchContext searchContext, Field field) {
        this.searchContext = searchContext;
        this.field = field;

        Annotations annotations = new Annotations(field);
        shouldCache = annotations.isLookupCached();

        by = buildByVaadinSelector();
        elementQuery = generateQuery();
    }

    @Override
    public WebElement findElement() {
        if (cachedElement != null && shouldCache) {
            return cachedElement;
        }

        WebElement element;
        if (by != null) {
            element = by.findElement(searchContext);
        } else {
            element = elementQuery.get(0);
        }
        if (shouldCache) {
            cachedElement = element;
        }

        return element;
    }

    @Override
    public List<WebElement> findElements() {
        if (cachedElementList != null && shouldCache) {
            return cachedElementList;
        }

        List<WebElement> elements;
        if (by != null) {
            elements = by.findElements(searchContext);
        } else {
            elements = (List<WebElement>) elementQuery.all();
        }
        if (shouldCache) {
            cachedElementList = elements;
        }

        return elements;
    }

    private ElementQuery<? extends AbstractElement> generateQuery() {
        assertValidAnnotations();

        ElementQuery<? extends AbstractElement> elementQuery = null;
        FindByVaadin findBy = field.getAnnotation(FindByVaadin.class);
        if (findBy != null) {
            Class<? extends AbstractElement> elementClass = (Class<? extends AbstractElement>) field
                    .getType();
            elementQuery = new ElementQuery<AbstractElement>(
                    (Class<AbstractElement>) elementClass);
            elementQuery.context(searchContext);
            elementQuery.recursive(findBy.recursive());
            if (findBy.index() != -1) {
                elementQuery.index(findBy.index());
            }
            if (!"".equals(findBy.id())) {
                elementQuery.id(findBy.id());
            }
            if (!"".equals(findBy.caption())) {
                elementQuery.caption(findBy.caption());
            }
        }

        System.err.println(elementQuery.generateQuery());
        return elementQuery;
    }

    private By buildByVaadinSelector() {
        assertValidAnnotations();

        FindByVaadin findBy = field.getAnnotation(FindByVaadin.class);
        if (findBy != null && !"".equals(findBy.vaadinSelector())) {
            return com.vaadin.testbench.By.vaadin(findBy.vaadinSelector());
        }
        return null;
    }

    private void assertValidAnnotations() {
        FindBys findBys = field.getAnnotation(FindBys.class);
        FindAll findAll = field.getAnnotation(FindAll.class);
        FindBy findBy = field.getAnnotation(FindBy.class);
        FindByVaadin findByVaadin = field.getAnnotation(FindByVaadin.class);
        if (findByVaadin != null
                && (findBy != null || findAll != null || findBys != null)) {
            throw new IllegalArgumentException(
                    "If you use a '@FindByVaadin' annotation, "
                            + "you must not also use a '@FindBy' annotation, "
                            + "a '@FindBys' annotation or a '@FindAll' annotation");
        }
    }

}
