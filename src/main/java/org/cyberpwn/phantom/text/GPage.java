package org.cyberpwn.phantom.text;

import org.cyberpwn.phantom.lang.GMap;

/**
 * Pages for books
 * 
 * @author cyberpwn
 */
public class GPage
{
	private GMap<String, String> elements;
	
	/**
	 * Create a page with headers for each paragraph
	 */
	public GPage()
	{
		this.elements = new GMap<String, String>();
	}
	
	/**
	 * Put a new paragraph of text into the book
	 * 
	 * @param title
	 *            the title
	 * @param paragraph
	 *            the text
	 * @return this (chain)
	 */
	public GPage put(String title, String paragraph)
	{
		if(title == null)
		{
			title = "";
		}
		
		if(paragraph == null)
		{
			paragraph = "";
		}
		
		elements.put(title, paragraph);
		return this;
	}
	
	public GMap<String, String> getElements()
	{
		return elements;
	}
	
	public void setElements(GMap<String, String> elements)
	{
		this.elements = elements;
	}
}
