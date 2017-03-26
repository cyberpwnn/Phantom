package org.phantomapi.pet;

import net.minecraft.server.v1_9_R2.AxisAlignedBB;
import net.minecraft.server.v1_9_R2.Entity;
import net.minecraft.server.v1_9_R2.EntityHuman;
import net.minecraft.server.v1_9_R2.EntityLiving;
import net.minecraft.server.v1_9_R2.EntityZombie;
import net.minecraft.server.v1_9_R2.World;

public class MiniEntityPet extends EntityZombie
{
	public MiniEntityPet(World world)
	{
		super(world);
	}
	
	@Override
	public void g(float paramFloat1, float paramFloat2)
	{
		EntityLiving localEntityLiving = (EntityLiving) bu();
		if(localEntityLiving == null)
		{
			for(Entity localEntity : passengers)
			{
				if((localEntity instanceof EntityHuman))
				{
					localEntityLiving = (EntityLiving) localEntity;
					P = 1.5F;
					break;
				}
			}
			
			if(localEntityLiving == null)
			{
				super.g(paramFloat1, paramFloat2);
				return;
			}
		}
		
		lastYaw = yaw;
		pitch = (pitch * 0.5F);
		setYawPitch(yaw, pitch);
		aP = (aN = yaw);
		paramFloat1 = (float) (be * 0.5D);
		paramFloat2 = bf;
		
		if(paramFloat2 <= 0.0F)
		{
			paramFloat2 = (float) (paramFloat2 * 0.5D);
		}
		
		l(0.25F);
		super.g(paramFloat1, paramFloat2);
		
		P = 1.0F;
	}
	
	@Override
	public void recalcPosition()
	{
		AxisAlignedBB vv = getBoundingBox();
		
		locX = ((vv.a + vv.d) / 2.0D);
		locZ = ((vv.c + vv.f) / 2.0D);
		locY = (vv.b - 1.5D);
	}
}
