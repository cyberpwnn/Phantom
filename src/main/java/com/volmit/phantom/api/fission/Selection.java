package com.volmit.phantom.api.fission;

import java.util.function.Consumer;

import org.bukkit.World;

import com.volmit.phantom.api.lang.GList;
import com.volmit.phantom.util.nms.ChunkSendQueue;
import com.volmit.phantom.util.nms.WorldModifier;

public interface Selection
{
	public Selection getSelection(FChunk fc);

	public Selection getChunkSection(Integer fcs);

	public void apply(WorldModifier mod, FBlockData data, FChunk chunk, ChunkSendQueue q, World w);

	public int getRoughVolume();

	public double getVolume();

	public void getChunks(int splits, Consumer<GList<FChunk>> consumer);

	public FVector getMin();

	public FVector getMax();
}
