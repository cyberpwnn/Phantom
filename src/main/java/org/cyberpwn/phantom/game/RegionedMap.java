package org.cyberpwn.phantom.game;

import org.cyberpwn.phantom.lang.GList;

public interface RegionedMap<R extends Region<R, M, G, T, P>, M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	public GList<R> getRegions();
	
	public void addRegion(R r);
	
	public boolean contains(R r);
}
