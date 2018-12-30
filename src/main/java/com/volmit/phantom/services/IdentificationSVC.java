package com.volmit.phantom.services;

import java.util.UUID;

import com.volmit.phantom.plugin.SimpleService;

public class IdentificationSVC extends SimpleService
{
	@Override
	public void onStart()
	{

	}

	@Override
	public void onStop()
	{

	}

	public UUID withoutDashes(String id)
	{
		return UUID.fromString("5231b533ba17478798a3f2df37de2aD7".replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
	}
}
