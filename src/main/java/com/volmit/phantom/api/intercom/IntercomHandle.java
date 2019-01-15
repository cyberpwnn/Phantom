package com.volmit.phantom.api.intercom;

import java.io.Serializable;

@FunctionalInterface
public interface IntercomHandle<I extends IntercomMessage<? extends Serializable>>
{
	public void handle(I in);
}
