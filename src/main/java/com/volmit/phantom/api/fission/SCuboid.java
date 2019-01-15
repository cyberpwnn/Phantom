package com.volmit.phantom.api.fission;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.World;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.util.nms.ChunkSendQueue;
import com.volmit.phantom.util.nms.WorldModifier;
import com.volmit.phantom.util.world.Cuboid;

public class SCuboid implements Selection
{
	protected int x1;
	protected int y1;
	protected int z1;
	protected int x2;
	protected int y2;
	protected int z2;

	public SCuboid(Cuboid c)
	{
		this(new FVector(c.getLowerNE()), new FVector(c.getUpperSW()));
	}

	public SCuboid(FVector a, FVector b)
	{
		this(a.getBlockX(), a.getBlockY(), a.getBlockZ(), b.getBlockX(), b.getBlockY(), b.getBlockZ());
	}

	public SCuboid(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		this.x1 = Math.min(x1, x2);
		this.y1 = M.iclip(Math.min(y1, y2), 0, 255);
		this.z1 = Math.min(z1, z2);
		this.x2 = Math.max(x1, x2);
		this.y2 = M.iclip(Math.max(y1, y2), 0, 255);
		this.z2 = Math.max(z1, z2);
	}

	@Override
	public Selection getChunkSection(Integer fcs)
	{
		int section = fcs << 4;
		int m1 = section;
		int m2 = section + 15;

		return new SCuboid(new FVector(x1, M.clip(y1, m1, m2), z1), new FVector(x2, M.clip(y2, m1, m2), z2));
	}

	@Override
	public Selection getSelection(FChunk fc)
	{
		int ax = fc.getX() << 4 | 0;
		int az = fc.getZ() << 4 | 0;
		int bx = fc.getX() << 4 | 15;
		int bz = fc.getZ() << 4 | 15;
		int cax = M.iclip(Math.max(ax, x1), ax, bx);
		int caz = Math.max(az, z1);
		int cbx = Math.min(bx, x2);
		int cbz = Math.min(bz, z2);
		int cay = y1;
		int cby = y2;

		return new SCuboid(new FVector(cax, cay, caz), new FVector(cbx, cby, cbz));
	}

	@Override
	public int getRoughVolume()
	{
		return (x2 - x1) * (y2 - y1) * (z2 - z1);
	}

	@Override
	public double getVolume()
	{
		return getRoughVolume();
	}

	@Override
	public void apply(WorldModifier mod, FBlockData data, FChunk chunk, ChunkSendQueue q, World w)
	{
		boolean[] cached = new boolean[16];
		Arrays.fill(cached, false);

		for(int y = y1; y <= y2; y++)
		{
			for(int x = x1; x <= x2; x++)
			{
				for(int z = z1; z <= z2; z++)
				{
					mod.setBlock(x, y, z, data.getId(), data.getData());
				}
			}

			cached[y >> 4] = true;
		}

		for(int i = 0; i < cached.length; i++)
		{
			if(cached[i])
			{
				mod.recalculateBlockCounts(chunk.getX(), i, chunk.getZ());
				q.queueSection(chunk.toBukkit(w), i);
			}
		}
	}

	@Override
	public void getChunks(int splits, Consumer<GList<FChunk>> f)
	{
		int minx = x1 >> 4;
		int minz = z1 >> 4;
		int maxx = x2 >> 4;
		int maxz = z2 >> 4;

		GList<FChunk> buffer = new GList<>();
		int total = (maxx - minx) * (maxz - minz);
		int per = Math.max(1, total / splits);

		for(int x = minx; x <= maxx; x++)
		{
			for(int z = minz; z <= maxz; z++)
			{
				buffer.add(new FChunk(x, z));

				if(buffer.size() >= per)
				{
					f.accept(buffer.copy());
					buffer.clear();
				}
			}
		}

		if(!buffer.isEmpty())
		{
			f.accept(buffer.copy());
		}
	}

	@Override
	public FVector getMin()
	{
		return new FVector(x1, y1, z1);
	}

	@Override
	public FVector getMax()
	{
		return new FVector(x2, y2, z2);
	}
}
