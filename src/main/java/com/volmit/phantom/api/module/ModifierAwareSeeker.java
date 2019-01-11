package com.volmit.phantom.api.module;

@FunctionalInterface
public interface ModifierAwareSeeker
{
	public boolean isValidModifiers(int mods);
}
