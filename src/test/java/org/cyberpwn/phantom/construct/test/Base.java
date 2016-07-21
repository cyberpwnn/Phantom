package org.cyberpwn.phantom.construct.test;

import org.bukkit.Location;
import org.cyberpwn.phantom.vfx.LineParticleManipulator;
import org.cyberpwn.phantom.vfx.ParticleEffect;
import org.cyberpwn.phantom.vfx.PhantomEffect;
import org.cyberpwn.phantom.vfx.SystemEffect;
import org.cyberpwn.phantom.vfx.VisualEffect;

public class Base
{
	//Lets make a line between A and B that makes 
	//a red and blue ball between the two points
	public Base(Location a, Location b)
	{
		//Lets create our visual effect (the point)
		//This point will have red and blue in it
		VisualEffect pointEffect = new SystemEffect();
		
		//Lets add some red
		pointEffect.addEffect(new PhantomEffect()
		{
			//Override the default play effect (nothing)
			public void play(Location l)
			{
				ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, l, 32);
			}
		});
		
		//And a little blue
		pointEffect.addEffect(new PhantomEffect()
		{
			//OVerride the default play effect
			public void play(Location l)
			{
				ParticleEffect.DRIP_WATER.display(0, 0, 0, 0, 1, l, 32);
			}
		});
		
		//Now that we have a playable point, we can make our line effect
		LineParticleManipulator lineOfBlueAndRedBalls = new LineParticleManipulator()
		{
			//Lets override the play on this line
			//This is played every point across the line
			public void play(Location p)
			{
				//We play our red and blue ball at the location passed in
				pointEffect.play(p);
			}
		};
		
		//Now lets finally draw the custom line
		//A for the first point
		//B for the second
		//The 2 tells the line to make 2 particles per block (density)
		lineOfBlueAndRedBalls.play(a, b, 2.0);
	}
}
