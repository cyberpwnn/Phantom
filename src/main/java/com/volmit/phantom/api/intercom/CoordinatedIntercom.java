package com.volmit.phantom.api.intercom;

import java.io.Serializable;

import com.volmit.phantom.api.lang.Callback;

public interface CoordinatedIntercom
{
	public String getServerName();

	public CoordinatedIntercom sendBlindly(Serializable s);

	public <TX extends Serializable, TD extends Serializable> CoordinatedIntercom sendMessage(TX s, Callback<TD> t);

	public <T extends Serializable> T sendNow(Serializable s, long ms);

	public void disconnect();

	public boolean isDead();

	public boolean hasWorkToDo();

	public void doWork();
}
