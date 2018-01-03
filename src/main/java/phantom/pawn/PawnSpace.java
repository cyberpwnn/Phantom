package phantom.pawn;

import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.util.exception.PawnActivationException;

/**
 * Holds active pawns
 *
 * @author cyberpwn
 */
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
					throw new PawnActivationException();
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
}
