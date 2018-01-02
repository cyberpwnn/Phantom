package phantom.pawn;

import java.lang.reflect.Method;

public class PawnMethod
{
	private final Method method;
	private final boolean async;
	private final int interval;
	private final PawnMethodType type;
	
	public PawnMethod(Method m)
	{
		method = m;
		async = m.isAnnotationPresent(Async.class);
		
		if(m.isAnnotationPresent(Tick.class))
		{
			type = PawnMethodType.TICK;
			interval = m.getAnnotation(Tick.class).value();
		}
		
		else if(m.isAnnotationPresent(Start.class))
		{
			type = PawnMethodType.START;
			interval = -1;
		}
		
		else if(m.isAnnotationPresent(Stop.class))
		{
			type = PawnMethodType.STOP;
			interval = -1;
		}
		
		else
		{
			type = null;
			interval = -1;
		}
	}

	public Method getMethod()
	{
		return method;
	}

	public boolean isAsync()
	{
		return async;
	}

	public int getInterval()
	{
		return interval;
	}

	public PawnMethodType getType()
	{
		return type;
	}
}
