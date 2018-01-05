package phantom.pawn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.phantomapi.Phantom;

import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.sched.TICK;

public class PawnObject
{
	private static int ct = 1;
	private final IPawn pawn;
	private final boolean registered;
	private boolean activated;
	private final GList<PawnMethod> methods;
	private final boolean singular;
	private final String name;
	private final GMap<Field, IPawn> pawns;
	private final GList<IPawn> hackedSubPawns;

	public PawnObject(IPawn p)
	{
		pawn = p;
		hackedSubPawns = new GList<IPawn>();
		Class<?> c = pawn.getClass();
		registered = c.isAnnotationPresent(Register.class);
		activated = false;
		methods = new GList<PawnMethod>();
		singular = c.isAnnotationPresent(Singular.class);
		String suff = singular ? "" : "-" + ct++;
		name = c.isAnnotationPresent(Name.class) ? c.getDeclaredAnnotation(Name.class).value() + suff : c.getSimpleName() + suff;
		pawns = new GMap<Field, IPawn>();

		for(Method i : c.getDeclaredMethods())
		{
			PawnMethod m = new PawnMethod(i);

			if(m.getType() != null)
			{
				methods.add(m);
			}
		}

		for(Field i : c.getDeclaredFields())
		{
			if(Modifier.isStatic(i.getModifiers()) || Modifier.isFinal(i.getModifiers()))
			{
				continue;
			}

			if(!i.isAnnotationPresent(Pawn.class))
			{
				continue;
			}

			i.setAccessible(true);

			try
			{
				IPawn spawn = (IPawn) i.getType().getConstructor().newInstance();
				pawns.put(i, spawn);
			}

			catch(Throwable e)
			{
				Phantom.kick(e);
			}
		}
	}

	public void forceOwnershipOf(IPawn pawn, Field f)
	{
		pawns.put(f, pawn);
	}

	public void forceOwnershipOf(IPawn pawn)
	{
		hackedSubPawns.add(pawn);
	}

	public void forceDestroyOwnershipOf(IPawn claimed)
	{
		hackedSubPawns.remove(pawn);

		for(Field i : pawns.k())
		{
			if(pawns.get(i).equals(pawn))
			{
				pawns.remove(i);
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
		activateSubPawns();

		if(registered)
		{
			Phantom.register(pawn);
		}

		callMethods(PawnMethodType.START);
	}

	private void activateSubPawns()
	{
		for(Field i : pawns.k())
		{
			try
			{
				IPawn instance = pawns.get(i);
				Phantom.activate(instance);
				i.setAccessible(true);
				i.set(pawn, instance);
			}

			catch(Exception e)
			{
				Phantom.kick(e);
			}
		}

		for(IPawn i : hackedSubPawns.copy())
		{
			Phantom.activate(i);
		}
	}

	public void deactivate()
	{
		deactivateSubPawns();

		if(registered)
		{
			Phantom.unregister(pawn);
		}

		callMethods(PawnMethodType.STOP);
	}

	private void deactivateSubPawns()
	{
		for(Field i : pawns.k())
		{
			try
			{
				IPawn instance = pawns.get(i);
				Phantom.deactivate(instance);
			}

			catch(Exception e)
			{
				Phantom.kick(e);
			}
		}

		for(IPawn i : hackedSubPawns.copy())
		{
			Phantom.deactivate(i);
		}
	}

	public void tick()
	{
		callMethods(PawnMethodType.TICK);
	}

	public IPawn getPawn()
	{
		return pawn;
	}

	public static int getCt()
	{
		return ct;
	}

	public GMap<Field, IPawn> getSubPawns()
	{
		return pawns;
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

	public boolean isSingular()
	{
		return singular;
	}

	public String getName()
	{
		return name;
	}
}
