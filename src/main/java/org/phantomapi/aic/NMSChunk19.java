package org.phantomapi.aic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_9_R2.CraftChunk;
import org.bukkit.entity.Player;
import org.phantomapi.nms.NMSX;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.ChunkSection;
import net.minecraft.server.v1_9_R2.DataBits;
import net.minecraft.server.v1_9_R2.IBlockData;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketDataSerializer;
import net.minecraft.server.v1_9_R2.PacketListener;

public class NMSChunk19 extends NMSChunk implements VirtualChunk
{
	private net.minecraft.server.v1_9_R2.Chunk nmsChunk;
	
	public NMSChunk19(Chunk bukkitChunk)
	{
		super(bukkitChunk);
		
		nmsChunk = ((CraftChunk) getChunk()).getHandle();
		pack();
	}
	
	@Override
	public void pack()
	{
		for(ChunkSection i : nmsChunk.getSections())
		{
			if(i == null)
			{
				continue;
			}
			
			if(i.getYPosition() > 15)
			{
				continue;
			}
			
			for(int j = 0; j < 16; j++)
			{
				for(int k = 0; k < 16; k++)
				{
					for(int l = 0; l < 16; l++)
					{
						IBlockData ibd = i.getBlocks().a(j, k, l);
						int id = Block.getId(ibd.getBlock());
						byte data = (byte) ibd.getBlock().toLegacyData(ibd);
						setSect(i.getYPosition(), j, k, l, id, data);
						skyLight[i.getYPosition()][getIndexAmod(j, k, l)] = (byte) i.getSkyLightArray().a(j, k, l);
						blockLight[i.getYPosition()][getIndexAmod(j, k, l)] = (byte) i.getEmittedLightArray().a(j, k, l);
					}
				}
			}
		}
		
		heightMap = Arrays.copyOf(nmsChunk.heightMap, nmsChunk.heightMap.length);
	}
	
	@Override
	public void send(Player p)
	{
		NMSX.sendPacket(p, new ChunkPacket(this));
	}
	
	@SuppressWarnings("rawtypes")
	public class ChunkPacket implements Packet
	{
		private NMSChunk19 c;
		
		public ChunkPacket(NMSChunk19 c)
		{
			this.c = c;
		}
		
		@Override
		public void b(PacketDataSerializer b) throws IOException
		{
			b.writeInt(getX());
			b.writeInt(getZ());
			b.writeBoolean(false);
			writeVarInt(getBitMask(), b);
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			NMOutputStream nm = new NMOutputStream(boas);
			int[][] data = c.blockData;
			
			for(int i = 0; i < data.length; i++)
			{
				int[] section = data[i];
				
				if(section == null)
				{
					continue;
				}
				
				int n = Block.REGISTRY_ID.a();
				nm.write(n);
				nm.writeVarInt(0);
				DataBits bits = new DataBits(n, 4096);
				
				for(int j = 0; j < 4096; j++)
				{
					int id = section[j];
					
					if(id != 0)
					{
						bits.a(j, id);
					}
				}
				
				for(long j : bits.a())
				{
					nm.writeLong(j);
				}
				
				nm.write(c.skyLight[i]);
				nm.write(c.blockLight[i]);
			}
			
			nm.close();
			writeVarInt(boas.size(), b);
			boas.writeTo(new ByteBufOutputStream(b));
			writeVarInt(0, b);
		}
		
		@Override
		public void a(PacketDataSerializer arg0) throws IOException
		{
			throw new UnsupportedOperationException("Deserialization not supported");
		}
		
		@Override
		public void a(PacketListener packetListener)
		{
			throw new UnsupportedOperationException("Listening not supported");
		}
		
		public void writeVarInt(int value, PacketDataSerializer b)
		{
			do
			{
				byte temp = (byte) (value & 0b01111111);
				value >>>= 7;
				
				if(value != 0)
				{
					temp |= 0b10000000;
				}
				
				b.writeByte(temp);
			}
			
			while(value != 0);
		}
	}
}
