package com.volmit.phantom.api.intercom;

import java.io.Serializable;

public interface IntercomHandler<I extends IntercomMessage<? extends Serializable>, O extends IntercomMessage<? extends Serializable>>
{
	public O handle(I in);

	public I createContainer();
}
