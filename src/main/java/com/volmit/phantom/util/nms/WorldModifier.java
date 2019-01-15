package com.volmit.phantom.util.nms;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import com.volmit.phantom.api.fission.FBlockData;
import com.volmit.phantom.api.fission.FVector;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.ChunkSection;
import net.minecraft.server.v1_12_R1.DataPaletteBlock;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.World;

public class WorldModifier
{
	private static final Field nonEmptyBlockCount = getDeclaredField(ChunkSection.class, "nonEmptyBlockCount");
	private static final Field tickingBlockCount = getDeclaredField(ChunkSection.class, "nonEmptyBlockCount");
	private static final Field blockIds = getDeclaredField(ChunkSection.class, "blockIds");
	private World world;

	public WorldModifier(org.bukkit.World world)
	{
		this.world = ((CraftWorld) world).getHandle();
	}

	public void setBlock(FVector v, FBlockData f)
	{
		setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), f.getId(), f.getData());
	}

	public void setBlock(int i, int j, int k, int id, byte data)
	{
		getPalette(getSection(i, j, k)).setBlock(i & 15, j & 15, k & 15, getBlockData(id, data));
	}

	public boolean hasBlocks(ChunkSection s)
	{
		return getNonEmptyBlockCount(s) > 0;
	}

	public boolean hasTickingBlocks(ChunkSection s)
	{
		return getTickingCount(s) > 0;
	}

	public int getTickingCount(ChunkSection s)
	{
		try
		{
			return tickingBlockCount.getInt(s);
		}

		catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

	public int getNonEmptyBlockCount(ChunkSection s)
	{
		try
		{
			return nonEmptyBlockCount.getInt(s);
		}

		catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

	public void recalculateBlockCounts(int sectionX, int sectionY, int sectionZ)
	{
		try
		{
			world.getChunkAt(sectionX, sectionZ).getSections()[sectionY].recalcBlockCounts();
		}

		catch(Throwable e)
		{

		}
	}

	private static DataPaletteBlock getPalette(ChunkSection section)
	{
		try
		{
			return (DataPaletteBlock) blockIds.get(section);
		}

		catch(IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public FBlockData getBlock(int i, int j, int k)
	{
		IBlockData d = getBlockRaw(i, j, k);
		Block b = d.getBlock();
		return new FBlockData(Block.getId(b), (byte) b.toLegacyData(d));
	}

	private IBlockData getBlockRaw(int i, int j, int k)
	{
		try
		{
			IBlockData data = getSection(i, j, k).getType(i & 15, j & 15, k & 15);

			if(data != null)
			{
				return data;
			}
		}

		catch(Throwable e)
		{

		}

		return getBlockData(0, (byte) 0);
	}

	@SuppressWarnings("deprecation")
	private static IBlockData getBlockData(int id, byte data)
	{
		return Block.getById(id).fromLegacyData(data);
	}

	private ChunkSection getSection(int i, int j, int k)
	{
		Chunk c = getChunk(i, k);
		ensureChunkSection(c, j);
		return c.getSections()[j >> 4];
	}

	private Chunk getChunk(int i, int k)
	{
		return world.getChunkAt(i >> 4, k >> 4);
	}

	private static void ensureChunkSection(Chunk c, int j)
	{
		if(c.getSections()[j >> 4] == null)
		{
			// TODO Chunks arent fully initialized for some reason?
			c.getSections()[j >> 4] = new net.minecraft.server.v1_12_R1.ChunkSection(j, c.world.worldProvider.m());
		}
	}

	private static final Field getDeclaredField(Class<?> c, String f)
	{
		try
		{
			Field fx = c.getDeclaredField(f);
			fx.setAccessible(true);
			return fx;
		}

		catch(Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
