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
 * 1.12.X NMS Wrapper<br/>
 * > Supports 1.12.2<br/>
 * > Supports 1.12.2-PRE<br/>
 * > Supports 1.12.1<br/>
 * > Supports 1.12
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Anchor("phantom-nmsw")
@Name("NMS Wrapper 1.12.X")
@NMSPackage("v1_12_R1")
public class NMSW12 extends NMSWrapper
{
	public NMSW12()
	{
		super(Protocol.R1_12.to(Protocol.R1_12_2));
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
