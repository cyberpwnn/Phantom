package com.volmit.phantom.api.module;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.volmit.phantom.util.text.C;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Color
{
	C value();
}
