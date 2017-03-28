package org.phantomapi.text;

import org.phantomapi.lang.GList;

public class TextFilterer implements TextFilter
{
	private GList<TextFilter> filters;
	
	public TextFilterer()
	{
		filters = new GList<TextFilter>();
	}
	
	public void registerFilter(TextFilter f)
	{
		filters.add(f);
	}
	
	@Override
	public String onFilter(String initial)
	{
		String f = initial;
		
		for(TextFilter i : filters)
		{
			f = i.onFilter(f);
		}
		
		return f;
	}
	
	public String filter(String initial)
	{
		return onFilter(initial);
	}
}
