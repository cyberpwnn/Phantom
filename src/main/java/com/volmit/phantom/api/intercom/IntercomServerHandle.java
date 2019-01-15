package com.volmit.phantom.api.intercom;

import java.io.Serializable;

@FunctionalInterface
public interface IntercomServerHandle<I extends IntercomMessage<? extends Serializable>, O extends IntercomMessage<? extends Serializable>>
{
	public O handle(I in);
}
