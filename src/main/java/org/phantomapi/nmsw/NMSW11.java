package org.phantomapi.nmsw;

import phantom.net.Protocol;
import phantom.nms.NMSPackage;
import phantom.nms.NMSWrapper;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.util.metrics.Anchor;
import phantom.util.metrics.Documented;

/**
 * 1.11.X NMS Wrapper<br/>
 * > Supports 1.11.2<br/>
 * > Supports 1.11.1<br/>
 * > Supports 1.11
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Anchor("phantom-nmsw")
@Name("NMS Wrapper 1.11.X")
@NMSPackage("v1_11_R1")
public class NMSW11 extends NMSWrapper
{
	public NMSW11()
	{
		super(Protocol.R1_11.to(Protocol.R1_11_2));
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
