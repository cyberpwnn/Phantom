package org.phantomapi.clust;

import java.util.UUID;

@Tabled("phantom_playerdata")
public class PlayerData extends ConfigurableObject
{
	public PlayerData(UUID uuid)
	{
		super(uuid.toString());
	}
}
