package phantom.pawn;

import java.lang.reflect.Method;
import phantom.lang.GList;

public class PawnObject
{
	private final IPawn pawn;
	private final boolean registered;
	private boolean activated;
	private final GList<PawnMethod> methods;

	public PawnObject(IPawn p)
	{
		pawn = p;
		Class<?> c = pawn.getClass();
		registered = c.isAnnotationPresent(Registered.class);
		activated = false;
		methods = new GList<PawnMethod>();
		
		for(Method i : c.getMethods())
		{
			PawnMethod m = new PawnMethod(i);
			
			if(m.getType() != null)
			{
				methods.add(m);
			}
		}
	}
	
	public void activate()
	{
		
	}
	
	public void deactivate()
	{
		
	}

	public IPawn getPawn()
	{
		return pawn;
	}

	public boolean isRegistered()
	{
		return registered;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public GList<PawnMethod> getMethods()
	{
		return methods;
	}
}
