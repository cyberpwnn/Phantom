package phantom.pawn;

import phantom.lang.GList;
import phantom.lang.GMap;

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
	 */
	public void activate(IPawn pawn)
	{
		activePawns.put(pawn, new PawnObject(pawn));
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
