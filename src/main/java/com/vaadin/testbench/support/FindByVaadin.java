package com.vaadin.testbench.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface FindByVaadin {

    String vaadinSelector() default "";

    boolean recursive() default true;

    String id() default "";

    String caption() default "";

    int index() default -1;
}
