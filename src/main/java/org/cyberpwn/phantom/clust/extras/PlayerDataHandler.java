package org.cyberpwn.phantom.clust.extras;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.clust.Configurable;
import org.cyberpwn.phantom.clust.DataController;
import org.cyberpwn.phantom.construct.Controllable;

/**
 * A PlayerDataController
 * 
 * @author cyberpwn
 * @param <C>
 *            the playerdata object
 */
public abstract class PlayerDataHandler<C extends Configurable> extends DataController<C, Player>
{
	public PlayerDataHandler(Controllable parentController)
	{
		super(parentController);
	}
	
	@Override
	public abstract C onLoad(Player identifier);
	
	@Override
	public abstract void onSave(Player identifier);
}
