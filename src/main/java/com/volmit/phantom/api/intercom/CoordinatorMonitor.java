package com.volmit.phantom.api.intercom;

public interface CoordinatorMonitor
{
	public void onConnected(CoordinatedIntercom intercom);

	public void onAuthenticated(CoordinatedIntercom intercom);

	public void onDisconnected(CoordinatedIntercom intercom);

	public void onReceive(CoordinatedIntercom intercom, Class<?> recv);

	public void onSend(CoordinatedIntercom intercom, Class<?> sent);
}
