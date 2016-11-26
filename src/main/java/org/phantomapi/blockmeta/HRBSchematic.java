package org.phantomapi.blockmeta;

import java.io.IOException;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataEntity;
import org.phantomapi.world.VectorSchematic;

public class HRBSchematic extends ConfigurableObject implements DataEntity
{
	private SchematicMeta meta;
	private VectorSchematic schematic;
	
	public HRBSchematic(String codeName)
	{
		super(codeName);
		
		meta = new SchematicMeta();
		schematic = new VectorSchematic();
	}
	
	public SchematicMeta getMeta()
	{
		return meta;
	}
	
	public VectorSchematic getSchematic()
	{
		return schematic;
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		getMeta().getConfiguration().flushLinks();
		String config = getConfiguration().toJSON().toString();
		String meta = getMeta().getConfiguration().toJSON().toString();
		String schematic = getSchematic().toConfiguration().toJSON().toString();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		// TODO Auto-generated method stub
		
	}
}
