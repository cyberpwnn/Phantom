package org.phantomapi.vfx;

import org.bukkit.Color;
import org.bukkit.Location;
import org.phantomapi.lang.GList;

public class ColoredParticleEffect implements VisualEffect
{
	private Color color;
	
	public ColoredParticleEffect(Color color)
	{
		this.color = color;
	}
	
	@Override
	public GList<VisualEffect> getEffects()
	{
		return new GList<VisualEffect>();
	}
	
	@Override
	public void play(Location l)
	{
		ParticleEffect.OrdinaryColor oc = new ParticleEffect.OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue());
		ParticleEffect.REDSTONE.display(oc, l, 32);
	}
	
	@Override
	public void addEffect(VisualEffect e)
	{
		
	}
}
