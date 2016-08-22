package org.phantomapi.slate;

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
public class PlaceholderSlate extends PhantomSlate
{
	private GMap<Player, IndividualSlate> inner;
	
	/**
	 * Create a placeholder slate
	 * 
	 * @param name
	 *            the name of the slate
	 */
	public PlaceholderSlate(String name)
	{
		super(name);
		
		inner = new GMap<Player, IndividualSlate>();
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
