package com.volmit.phantom.api.fission;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.World;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.util.nms.ChunkSendQueue;
import com.volmit.phantom.util.nms.WorldModifier;

public class SSphere extends SCuboid implements Selection
{
	private boolean solid;
	private int ox;
	private int oy;
	private int oz;
	private double radius;

	public SSphere(int x, int y, int z, boolean solid, double radius)
	{
		super(x - (int) (radius), y - (int) (radius), z - (int) (radius), x + (int) (radius), y + (int) (radius), z + (int) (radius));
		this.solid = solid;
		this.radius = radius;
		this.ox = x;
		this.oy = y;
		this.oz = z;
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

		SSphere f = new SSphere(ox, oy, oz, solid, radius);
		f.x1 = cax;
		f.x2 = cbx;
		f.y1 = cay;
		f.y2 = cby;
		f.z1 = caz;
		f.z2 = cbz;

		return f;
	}

	@Override
	public void apply(WorldModifier mod, FBlockData data, FChunk chunk, ChunkSendQueue q, World w)
	{
		boolean[] cached = new boolean[16];
		Arrays.fill(cached, false);
		double sqrd = Math.pow(radius, 2);

		for(int y = y1; y <= y2; y++)
		{
			for(int x = x1; x <= x2; x++)
			{
				for(int z = z1; z <= z2; z++)
				{
					double d = new FVector(x, y, z).distanceSq(new FVector(ox, oy, oz));

					if(solid && d <= sqrd)
					{
						mod.setBlock(x, y, z, data.getId(), data.getData());
					}

					if(!solid && Math.ceil(d) == Math.ceil(sqrd))
					{
						mod.setBlock(x, y, z, data.getId(), data.getData());
					}
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

	@Override
	public int getRoughVolume()
	{
		return (int) ((4D * Math.PI * Math.pow(radius, 3)) / 3D);
	}

	@Override
	public double getVolume()
	{
		return this.getRoughVolume();
	}
}
