package org.phantomapi.blockmeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.DataEntity;
import org.phantomapi.clust.JSONObject;
import org.phantomapi.lang.GList;
import org.phantomapi.nest.Nest;
import org.phantomapi.world.Cuboid;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.VariableBlock;
import org.phantomapi.world.VectorSchematic;
import org.phantomapi.world.WQ;

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
	
	/**
	 * Write data from this schematic to a file
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             shit happens
	 */
	public void save(File file) throws IOException
	{
		Files.write(file.toPath(), toData());
	}
	
	/**
	 * Load the given file into the schematic
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             shit happens
	 */
	public void load(File file) throws IOException
	{
		fromData(Files.readAllBytes(file.toPath()));
	}
	
	/**
	 * Paste the schematic based on the reference point
	 * 
	 * @param point
	 *            the point
	 */
	public void paste(Location point)
	{
		WQ q = new WQ();
		
		for(Vector i : getSchematic().getSchematic().k())
		{
			Block b = point.clone().add(i).getBlock();
			Nest.getBlock(b).clear();
			Nest.getBlock(b).setData(getMeta().getBlock(i).getConfiguration().copy().getData());
			q.set(b.getLocation(), getSchematic().getSchematic().get(i).getBlocks().pickRandom());
		}
		
		q.flush();
	}
	
	/**
	 * Copy the world data into the schematic based on the reference point
	 * 
	 * @param cuboid
	 *            the cuboid selection
	 * @param point
	 *            the reference point
	 */
	@SuppressWarnings("deprecation")
	public void copy(Cuboid cuboid, Location point)
	{
		getSchematic().clear();
		getMeta().clear();
		GList<Block> blocks = new GList<Block>(cuboid.iterator());
		Vector cursor = new Vector(0, 0, 0);
		Block last = null;
		Block root = null;
		
		while(!blocks.isEmpty())
		{
			Block b = blocks.pop();
			
			if(last != null)
			{
				cursor = cursor.clone().add(b.getLocation().subtract(last.getLocation()).toVector());
			}
			
			DataCluster cc = Nest.getBlock(b).copy();
			MaterialBlock mb = new MaterialBlock(b.getType(), b.getData());
			getSchematic().getSchematic().put(cursor, new VariableBlock(mb));
			getMeta().getBlock(cursor).getConfiguration().setData(cc.getData());
			last = b;
			
			if(cursor.equals(new Vector(0, 0, 0)))
			{
				root = b;
			}
		}
		
		setReferencePoint(root.getLocation().clone().subtract(point).toVector());
	}
	
	/**
	 * Set the reference point
	 * 
	 * @param v
	 *            the vector
	 */
	public void setReferencePoint(Vector v)
	{
		getConfiguration().set("r.x", v.getBlockX());
		getConfiguration().set("r.y", v.getBlockY());
		getConfiguration().set("r.z", v.getBlockZ());
	}
	
	/**
	 * Get the reference point from the vector 0,0,0 in the mapping
	 * 
	 * @return the reference point, or 0,0,0
	 */
	public Vector getReferencePoint()
	{
		if(getConfiguration().contains("r.x"))
		{
			return new Vector(getConfiguration().getInt("r.x"), getConfiguration().getInt("r.y"), getConfiguration().getInt("r.z"));
		}
		
		return new Vector(0, 0, 0);
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
