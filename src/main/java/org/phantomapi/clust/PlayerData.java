package org.phantomapi.clust;

import java.util.UUID;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;

@Tabled("phantom_playerdata")
public class PlayerData extends ConfigurableObject
{
	public PlayerData(UUID uuid)
	{
		super(uuid.toString());
	}
	
	public void flush()
	{
		new A()
		{
			@Override
			public void async()
			{
				Phantom.instance().getPdm().flush(PlayerData.this);
			}
		};
	}
}
