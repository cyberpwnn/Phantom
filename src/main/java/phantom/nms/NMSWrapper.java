package phantom.nms;

import phantom.net.Protocol;
import phantom.net.ProtocolRange;
import phantom.util.metrics.Documented;

/**
 * A basic nms wrapper
 *
 * @author cyberpwn
 */
@Documented
public class NMSWrapper implements INMSWrapper
{
	private ProtocolRange range;

	/**
	 * Create an nms wrapper
	 *
	 * @param range
	 *            the target range
	 */
	public NMSWrapper(ProtocolRange range)
	{
		this.range = range;
	}

	@Override
	public Protocol getTargetProtocol()
	{
		return getProtocolRange().getTo();
	}

	@Override
	public ProtocolRange getProtocolRange()
	{
		return range;
	}

	@Override
	public String getPackageVersion()
	{
		return getTargetProtocol().getPackageVersion();
	}
}
