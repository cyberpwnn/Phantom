package com.volmit.phantom.api.intercom;

@FunctionalInterface
public interface InboxExecutor<M>
{
	public void onInboxMessageReceived(M m);
}
