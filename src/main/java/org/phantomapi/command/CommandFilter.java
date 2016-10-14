package org.phantomapi.command;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A Command Filter
 * 
 * @author cyberpwn
 */
public @interface CommandFilter
{
	/**
	 * Filters the argument ranges
	 * 
	 * @author cyberpwn
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface ArgumentRange
	{
		int[] value();
	}
	
	/**
	 * Define sub commands that must be filled to fire this command
	 * 
	 * @author cyberpwn
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface SubCommands
	{
		String[] value();
	}
	
	/**
	 * Define the tag for this command response
	 * 
	 * @author cyberpwn
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Tag
	{
		String value();
	}
	
	/**
	 * Define the tag hover for this command response
	 * 
	 * @author cyberpwn
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface TagHover
	{
		String value();
	}
	
	/**
	 * Filter players only
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface PlayerOnly
	{
		
	}
	
	/**
	 * Filter players only
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface ConsoleOnly
	{
		
	}
	
	/**
	 * Filter ops only
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface OperatorOnly
	{
		
	}
	
	/**
	 * Filter based on a required permission
	 * 
	 * @author cyberpwn
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Permission
	{
		String value();
	}
	
	/**
	 * Filter based on multiple required permissions
	 * 
	 * @author cyberpwn
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface Permissions
	{
		String[] value();
	}
}
