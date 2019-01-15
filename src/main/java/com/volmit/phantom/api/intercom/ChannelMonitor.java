package com.volmit.phantom.api.intercom;

public interface ChannelMonitor
{
	public void onConnected();

	public void onAuthenticated();

	public void onAuthenticationFailure();

	public void onDisconnected();

	public void onReceive(Class<?> recv);

	public void onSend(Class<?> sent);
}
