package com.volmit.phantom.api.intercom;

import java.io.Serializable;

@FunctionalInterface
public interface IntercomServerHelper<I extends Serializable, O extends Serializable>
{
	public O handle(I in);
}
