package com.volmit.phantom.util;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;

@Retention(SOURCE)
public @interface Dangerous
{
	public String value() default "";
}
