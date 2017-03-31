package org.phantomapi.library;

import java.net.URL;

public class WraithCoordinate implements Coordinate
{
	private String version;
	private String groupId;
	private String artifactId;
	private Repository repository;
	
	public WraithCoordinate(String groupId, String artifactId, String version, Repository repository)
	{
		this.version = version;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.repository = repository;
	}
	
	@Override
	public String getVersion()
	{
		return version;
	}
	
	@Override
	public String getGroupId()
	{
		return groupId;
	}
	
	@Override
	public String getArtifactId()
	{
		return artifactId;
	}
	
	@Override
	public Repository getRepository()
	{
		return repository;
	}
	
	@Override
	public String getEffectiveName()
	{
		return getArtifactId() + "-" + getVersion() + ".jar";
	}
	
	@Override
	public String getEffectivePath()
	{
		return getGroupId().replaceAll("\\.", "/") + "/" + getArtifactId() + "/" + getVersion();
	}
	
	@Override
	public URL getEffectiveURL()
	{
		return getRepository().getUrl(this);
	}
	
	@Override
	public String toString()
	{
		return getGroupId() + ":" + getArtifactId() + ":" + getVersion();
	}
}
