package com.zcode.demo.mytest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhouwb
 * @since 2019/11/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Employee {

    public String id() default "000";

    public String name() default "employee";

    public String type() default "1";

}
