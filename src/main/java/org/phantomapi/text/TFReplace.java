package org.phantomapi.text;

public class TFReplace implements TextFilter
{
	private String grep;
	private String rep;
	
	public TFReplace(String grep, String rep)
	{
		this.grep = grep;
		this.rep = rep;
	}
	
	@Override
	public String onFilter(String initial)
	{
		return initial.replaceAll(grep, rep);
	}
}
