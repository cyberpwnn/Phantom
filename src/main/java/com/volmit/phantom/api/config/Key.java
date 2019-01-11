package com.volmit.phantom.api.config;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Represents a key for an object config
 *
 * @author cyberpwn
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Key
{
	String value() default "";
}
