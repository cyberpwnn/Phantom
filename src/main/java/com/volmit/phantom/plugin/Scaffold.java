package com.volmit.phantom.plugin;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.volmit.phantom.text.C;

public class Scaffold
{
	@Retention(RUNTIME)
	@Target(TYPE)
	public @interface ModuleInfo
	{
		String name();

		String version()

		default "1.0.0";

		String author()

		default "A Phantom";

		C color() default C.WHITE;
	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public @interface Instance
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Test
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface ConsoleTest
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface PlayerTest
	{

	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public @interface Service
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Async
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Start
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Stop
	{

	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Tick
	{
		int value() default 0;
	}

	@Retention(RUNTIME)
	@Target(FIELD)
	public @interface Command
	{
		String value() default "";
	}

}
