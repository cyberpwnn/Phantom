package phantom.pawn;

import java.lang.reflect.Field;

import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.lang.format.F;
import phantom.util.exception.PawnActivationException;
import phantom.util.metrics.Documented;

/**
 * Holds active pawns
 *
 * @author cyberpwn
 */
@Documented
public class PawnSpace
{
	private final GMap<IPawn, PawnObject> activePawns;

	/**
	 * Create a new pawn space
	 */
	public PawnSpace()
	{
		activePawns = new GMap<IPawn, PawnObject>();
	}

	/**
	 * Get active pawns
	 *
	 * @return returns a glist of active pawns
	 */
	public GList<IPawn> getActivePawns()
	{
		return activePawns.k();
	}

	/**
	 * Activate a pawn
	 *
	 * @param pawn
	 *            the pawn to activate
	 *
	 * @throws PawnActivationException
	 *             duplicate instances of a singularity pawn
	 */
	public void activate(IPawn pawn) throws PawnActivationException
	{
		PawnObject pw = new PawnObject(pawn);

		if(pw.isSingular())
		{
			for(IPawn i : getActivePawns())
			{
				if(activePawns.get(i).isSingular() && activePawns.get(i).getName().equals(pw.getName()))
				{
					throw new PawnActivationException(pw.getName() + " is already active. Cannot have multiple @Singular pawns");
				}
			}
		}

		activePawns.put(pawn, pw);
		activePawns.get(pawn).activate();
	}

	/**
	 * Deactivate a pawn
	 *
	 * @param pawn
	 *            the pawn to deactivate
	 */
	public void deactivate(IPawn pawn)
	{
		if(!activePawns.containsKey(pawn))
		{
			return;
		}

		activePawns.get(pawn).deactivate();
		activePawns.remove(pawn);
	}

	/**
	 * Tick the pawns
	 */
	public void tick()
	{
		for(IPawn i : activePawns.k())
		{
			activePawns.get(i).tick();
		}
	}

	/**
	 * Defines that the OWNER is claiming super-pawn over the CLAIMED sub-pawn which
	 * is trusted to be held in the field FIELD
	 *
	 * @param owner
	 *            the owner or super-pawn)
	 * @param field
	 *            the field which the super-pawn has that holds the sub-pawn
	 *            (claimed)
	 * @param claimed
	 *            the claimed (sub-pawn) which is held in the given field
	 */
	public void claim(IPawn owner, Field field, IPawn claimed)
	{
		activePawns.get(owner).forceOwnershipOf(claimed, field);
	}

	/**
	 * Prints all singularities down from the given host
	 *
	 * @param host
	 *            show the host
	 * @return the list of strings to print
	 */
	public GList<String> printSingularitySpace(IPawn host)
	{
		return findSubPawns(host, 0);
	}

	private GList<String> findSubPawns(IPawn host, int deep)
	{
		GList<String> dv = new GList<String>();

		if(deep == 0)
		{
			dv.add(activePawns.get(host).getName());
			deep++;
		}

		for(IPawn i : activePawns.get(host).getSubPawns().v())
		{
			dv.add(F.repeat("  ", deep) + (deep == 0 ? "" : "->") + activePawns.get(i).getName());
			dv.addAll(findSubPawns(i, deep + 1));
		}

		return dv;
	}

	public boolean isActive(IPawn pawn)
	{
		String name = pawn.getClass().isAnnotationPresent(Name.class) ? pawn.getClass().getDeclaredAnnotation(Name.class).value() : null;

		if(name == null)
		{
			return false;
		}

		for(IPawn i : getActivePawns())
		{
			if(activePawns.get(i).getName().equals(name))
			{
				return true;
			}
		}

		return false;
	}
}
