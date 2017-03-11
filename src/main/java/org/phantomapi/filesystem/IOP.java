package org.phantomapi.filesystem;

import org.bukkit.ChatColor;
import org.phantomapi.util.D;

public abstract class IOP implements FOP
{
	private D d;
	protected FileHack h;
	
	public IOP(FileHack h)
	{
		this.h = h;
		d = new D("IOP<" + getClass().getSimpleName() + ">");
	}
	
	public void queue(FOP f)
	{
		h.queue(f);
	}
	
	@Override
	public void log(String op, CharSequence... s)
	{
		String m = op + ": ";
		
		for(CharSequence i : s)
		{
			m = m + ChatColor.RED + i + ChatColor.YELLOW + " -> ";
		}
		
		d.w(m);
	}
}
