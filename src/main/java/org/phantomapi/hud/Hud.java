package org.phantomapi.hud;

import org.phantomapi.lang.GList;

public interface Hud
{
	public void open();
	
	public void close();
	
	public void setContents(GList<String> options);
	
	public GList<String> getContents();
	
	public String getSelection();
	
	public int getSelectionRow();
}
