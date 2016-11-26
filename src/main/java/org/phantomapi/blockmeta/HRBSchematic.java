package org.phantomapi.blockmeta;

import java.io.IOException;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataEntity;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.world.VectorSchematic;

/**
 * Represents a schematic which holds block data and meta
 * 
 * @author cyberpwn
 */
public class HRBSchematic extends ConfigurableObject implements DataEntity
{
	private SchematicMeta meta;
	private VectorSchematic schematic;
	
	/**
	 * Create an hrb schematic
	 * 
	 * @param codeName
	 *            give it a name
	 */
	public HRBSchematic(String codeName)
	{
		super(codeName);
		
		meta = new SchematicMeta();
		schematic = new VectorSchematic();
	}
	
	/**
	 * Get the schematic meta holder
	 * 
	 * @return the schematic meta instance
	 */
	public SchematicMeta getMeta()
	{
		return meta;
	}
	
	/**
	 * Get the vector schematic holding the actual blocks
	 * 
	 * @return the vector schematic
	 */
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
		DataCluster cc = new DataCluster();
		cc.set("c", config);
		cc.set("m", meta);
		cc.set("s", schematic);
		
		return cc.compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		DataCluster cc = new DataCluster(data);
		String config = cc.getString("c");
		String meta = cc.getString("m");
		String schematic = cc.getString("s");
		getConfiguration().clear();
		getConfiguration().addJson(new JSONObject(config));
		getMeta().getConfiguration().clear();
		getMeta().getConfiguration().addJson(new JSONObject(meta));
		getSchematic().fromConfiguration(new DataCluster(new JSONObject(schematic)));
	}
}
