package org.phantomapi.nmsw;

import phantom.net.Protocol;
import phantom.nms.NMSPackage;
import phantom.nms.NMSWrapper;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.util.metrics.Documented;

/**
 * Empty NMS Wrapper<br/>
 * > Supports Nothing
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("NMS Wrapper Unsupported")
@NMSPackage("UNKNOWN")
public class NMSWX extends NMSWrapper
{
	public NMSWX()
	{
		super(Protocol.EARLIEST.to(Protocol.R1_7_10));
	}

	@Start
	public void onStart()
	{

	}

	@Stop
	public void onStop()
	{

	}
}
