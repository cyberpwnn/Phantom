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
 * 1.10.X NMS Wrapper<br/>
 * > Supports 1.10.2<br/>
 * > Supports 1.10.1<br/>
 * > Supports 1.10
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("NMS Wrapper 1.10.X")
@NMSPackage("v1_10_R1")
public class NMSW10 extends NMSWrapper
{
	public NMSW10()
	{
		super(Protocol.R1_10.to(Protocol.R1_10_2));
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
