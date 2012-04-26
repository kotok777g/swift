/*
 * Copyright 2004-present Facebook. All Rights Reserved.
 */
package com.facebook.miffed;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface ThriftField
{
    short id() default Short.MIN_VALUE;

    String name() default "";

    boolean required() default false;

    ThriftProtocolFieldType protocolType() default ThriftProtocolFieldType.STOP;
}