package org.phantomapi.hud;

import org.bukkit.entity.Player;
import org.phantomapi.gui.Click;
import org.phantomapi.lang.GList;

public interface Hud
{
	public void open();
	
	public void close();
	
	public void setContents(GList<String> options);
	
	public GList<String> getContents();
	
	public String getSelection();
	
	public int getSelectionRow();
	
	public void onUpdate();
	
	public void onOpen();
	
	public String onDisable(String s);
	
	public String onEnable(String s);
	
	public void onClose();
	
	public void onSelect(String selection, int slot);
	
	public void onClick(Click c, Player p, String selection, int slot, Hud h);
}
