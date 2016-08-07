package org.cyberpwn.phantom.transmit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Filter the transmission channel for any transmission events
 * 
 * @author cyberpwn
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface TransmissionFilter
{
	public String value();
}
