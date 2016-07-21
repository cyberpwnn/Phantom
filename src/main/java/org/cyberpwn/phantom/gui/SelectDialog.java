package org.cyberpwn.phantom.gui;

public interface SelectDialog<T>
{
	public SelectDialog<T> bind(Element e, T t);
	public void onSelected(T clicked, Element e);
}
