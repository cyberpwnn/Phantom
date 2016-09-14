package org.phantomapi.game;

import org.phantomapi.construct.Ghost;

public abstract class GamePlugin extends Ghost
{
	@Override
	public abstract void preStart();

	@Override
	public abstract void onStart();

	@Override
	public abstract void onStop();

	@Override
	public abstract void postStop();
}
