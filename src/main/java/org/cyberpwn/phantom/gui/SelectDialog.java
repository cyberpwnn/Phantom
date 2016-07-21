package org.cyberpwn.phantom.gui;

/**
 * Selection dialog
 * 
 * @author cyberpwn
 *
 * @param <T>
 *            the type of object to be bound to objects
 */
public interface SelectDialog<T>
{
	/**
	 * Bind an element to an object of the type T
	 * 
	 * @param e
	 *            the element
	 * @param t
	 *            the object
	 * @return this
	 */
	public SelectDialog<T> bind(Element e, T t);
	
	/**
	 * Called when a player selects a bound element
	 * 
	 * @param clicked
	 *            the clicked object (bound to the clicked element)
	 * @param e
	 *            the clicked element
	 */
	public void onSelected(T clicked, Element e);
}
