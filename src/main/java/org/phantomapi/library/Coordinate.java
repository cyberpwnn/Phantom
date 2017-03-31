package org.phantomapi.library;

import java.net.MalformedURLException;
import java.net.URL;

public interface Coordinate
{
	public String getVersion();
	
	public String getGroupId();
	
	public String getArtifactId();
	
	public Repository getRepository();
	
	public String getEffectiveName();
	
	public String getEffectivePath();
	
	public URL getEffectiveURL();
	
	public static Coordinate create(String groupId, String artifactId, String version, String repository) throws MalformedURLException
	{
		return new WraithCoordinate(groupId, artifactId, version, new WraithRepository(new URL(repository)));
	}
	
	public static Coordinate create(String groupId, String artifactId, String version, Repository repository)
	{
		return new WraithCoordinate(groupId, artifactId, version, repository);
	}
}
