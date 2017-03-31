package org.phantomapi.library;

import java.net.URL;

public interface Library
{
	public LibraryState getState();
	
	public boolean isInstalled();
	
	public Coordinate getCoordinates();
	
	public String getVersion();
	
	public String getArtifactId();
	
	public String getGroupId();
	
	public Repository getRepository();
	
	public URL getEffectiveURL();
}
