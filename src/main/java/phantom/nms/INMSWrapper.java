package phantom.nms;

import phantom.net.Protocol;
import phantom.net.ProtocolRange;
import phantom.pawn.IPawn;
import phantom.util.metrics.Documented;

/**
 * Basic nms wrapper
 *
 * @author cyberpwn
 */
@Documented
public interface INMSWrapper extends IPawn
{
	/**
	 * Get the target protocol
	 *
	 * @return the target
	 */
	public Protocol getTargetProtocol();

	/**
	 * The protocol range
	 *
	 * @return the range
	 */
	public ProtocolRange getProtocolRange();

	/**
	 * The package version
	 *
	 * @return a string representing the package version
	 */
	public String getPackageVersion();
}
