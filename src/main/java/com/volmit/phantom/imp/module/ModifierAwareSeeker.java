package com.volmit.phantom.imp.module;

@FunctionalInterface
public interface ModifierAwareSeeker
{
	public boolean isValidModifiers(int mods);
}
