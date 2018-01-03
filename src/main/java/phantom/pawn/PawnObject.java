package phantom.pawn;

import java.lang.reflect.Method;
import org.phantomapi.Phantom;
import phantom.lang.GList;
import phantom.scheduler.TICK;

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
	
	private void callMethods(PawnMethodType type)
	{
		for(PawnMethod i : methods)
		{
			if(i.getType().equals(type))
			{
				invokeMethod(i);
			}
		}
	}
	
	private void invokeMethod(PawnMethod m)
	{
		if(m.getType().equals(PawnMethodType.TICK) && TICK.tick % m.getInterval() != 0)
		{
			return;
		}
		
		if(m.isAsync())
		{
			// TODO in async
			invokeMethod(m.getMethod());
		}
		
		else
		{
			invokeMethod(m.getMethod());
		}
	}
	
	private void invokeMethod(Method m)
	{
		try
		{
			m.invoke(pawn);
		}
		
		catch(Throwable e)
		{
			Phantom.kick(e);
		}
	}
	
	public void activate()
	{
		if(registered)
		{
			Phantom.register(pawn);
		}
		
		callMethods(PawnMethodType.START);
	}
	
	public void deactivate()
	{
		if(registered)
		{
			Phantom.unregister(pawn);
		}
		
		callMethods(PawnMethodType.STOP);
	}
	
	public void tick()
	{
		callMethods(PawnMethodType.TICK);
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
