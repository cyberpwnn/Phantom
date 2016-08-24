package org.phantomapi.slate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.util.F;

/**
 * Represents a slate which creates sub-slates for placeholders to all viewers
 * of this slate
 * 
 * @author cyberpwn
 */
public class PlaceholderSlate implements Slate
{
	private GList<String> lines;
	protected GList<Player> viewers;
	private String name;
	private GMap<Player, IndividualSlate> inner;
	
	/**
	 * Create a placeholder slate
	 * 
	 * @param name
	 *            the name of the slate
	 */
	public PlaceholderSlate(String name)
	{
		this.name = name;
		this.lines = new GList<String>();
		this.viewers = new GList<Player>();		
		this.inner = new GMap<Player, IndividualSlate>();
	}
	
	@Override
	public GList<String> getLines()
	{
		return lines;
	}
	
	@Override
	public void set(int index, String line)
	{
		lines.set(index, line);
	}
	
	@Override
	public void setLines(GList<String> lines)
	{
		this.lines = lines;
	}
	
	@Override
	public String get(int index)
	{
		return lines.hasIndex(index) ? lines.get(index) : null;
	}
	
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void addViewer(Player p)
	{
		if(!isViewing(p))
		{
			viewers.add(p);
		}
	}

	@Override
	public void removeViewer(Player p)
	{
		viewers.remove(p);
		p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@Override
	public GList<Player> getViewers()
	{
		return viewers.copy();
	}

	@Override
	public boolean isViewing(Player p)
	{
		return viewers.contains(p);
	}

	@Override
	public void addLine(String s)
	{
		lines.add(s);
	}

	@Override
	public void clearLines()
	{
		lines.clear();
	}
	
	/**
	 * Updates and builds all sub-slates and updates the player with their
	 * respective
	 * sub-slates.
	 */
	@Override
	public void update()
	{
		build();
		
		for(Player i : viewers)
		{
			inner.get(i).update();
		}
	}
	
	/**
	 * Builds all sub-slates with placeholder values
	 */
	@Override
	public void build()
	{
		for(Player i : viewers)
		{
			IndividualSlate ins = new IndividualSlate(getName(), i);
			GList<String> lns = new GList<String>();
			
			for(String j : getLines())
			{
				lns.add(F.p(i, j));
			}
			
			ins.setLines(lns);
			inner.put(i, ins);
		}
	}
}
