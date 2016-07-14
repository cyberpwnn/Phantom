package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.construct.Controllable;
import org.cyberpwn.phantom.construct.Controller;

public class PhantomGameHandler<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>> extends Controller implements GameHandler<M, G, T, P>
{
	public PhantomGameHandler(Controllable parentController)
	{
		super(parentController);
		
		if(!(parentController instanceof Game))
		{
			throw new RuntimeException(getClass().getSimpleName() + " Cannot handle a non game controller. (" + parentController.getClass().getSimpleName() + ")");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public G getGame()
	{
		return (G) parentController;
	}

	@Override
	public M getMap()
	{
		return getGame().getMap();
	}
}
