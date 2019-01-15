package com.volmit.phantom.api.intercom;

import java.io.Serializable;

import com.volmit.phantom.api.lang.Callback;

public interface IntercomChannel
{
	public IntercomChannel addMonitor(ChannelMonitor monitor);

	public IntercomChannel removeMonitor(ChannelMonitor monitor);

	public <T extends Serializable, X extends Serializable> IntercomChannel accept(Class<? extends T> c, IntercomServerHelper<T, X> h);

	public <TX extends Serializable, TD extends Serializable> IntercomChannel sendMessage(TX s, Callback<TD> t);

	public IntercomChannel sendBlindly(Serializable s);

	public <T extends Serializable> T sendNow(Serializable s, long ms);

	public <T extends Serializable> IntercomChannel addInboxExecutor(Class<? extends T> s, InboxExecutor<T> t);

	public void disconnect();
}
