package org.phantomapi.text;

/**
 * Provides chat tags
 * 
 * @author cyberpwn
 */
public interface TagProvider
{
	/**
	 * Get the chat tag for this object
	 * 
	 * @return the object
	 */
	public String getChatTag();
	
	/**
	 * Get the chat hover tag for this object
	 * 
	 * @return the object
	 */
	public String getChatTagHover();
}
