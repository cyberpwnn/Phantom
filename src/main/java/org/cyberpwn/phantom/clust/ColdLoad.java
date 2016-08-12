package org.cyberpwn.phantom.clust;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DO NOT Hotload config changes
 * 
 * @author cyberpwn
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColdLoad
{
	
}
