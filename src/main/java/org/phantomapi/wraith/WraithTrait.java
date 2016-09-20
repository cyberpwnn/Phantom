package org.phantomapi.wraith;

import net.citizensnpcs.api.trait.Trait;

public abstract class WraithTrait extends Trait
{
	protected WraithTrait(String name)
	{
		super(name);
	}

	@Override
	public abstract void onAttach();

	@Override
	public abstract void onDespawn();

	@Override
	public abstract void onSpawn();

	@Override
	public abstract void run();
}
