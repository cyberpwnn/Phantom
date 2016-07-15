package org.cyberpwn.phantom.game;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.lang.Alphabet;

public interface GameAdapter<M extends GameMap<M, G, T, P>, G extends Game<M, G, T, P>, T extends Team<M, G, T, P>, P extends GamePlayer<M, G, T, P>>
{
	public G onGameCreate(Alphabet alphabet);
	public P onPlayerObjectCreate(G g, Player p);
}
