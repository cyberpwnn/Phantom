package phantom.pawn;

import phantom.lang.GList;

/**
 * Holds active pawns
 *
 * @author cyberpwn
 */
public class PawnSpace
{
	private final GList<IPawn> activePawns;

	/**
	 * Create a new pawn space
	 */
	public PawnSpace()
	{
		activePawns = new GList<IPawn>();
	}

	/**
	 * Activate a pawn
	 * 
	 * @param pawn
	 *            the pawn to activate
	 */
	public void activate(IPawn pawn)
	{
		activePawns.add(pawn);
	}

	/**
	 * Deactivate a pawn
	 * 
	 * @param pawn
	 *            the pawn to deactivate
	 */
	public void deactivate(IPawn pawn)
	{
		activePawns.remove(pawn);
	}
}
