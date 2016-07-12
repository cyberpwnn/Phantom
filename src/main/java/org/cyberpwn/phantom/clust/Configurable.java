package org.cyberpwn.phantom.clust;

public interface Configurable
{
	void onNewConfig();
	void onReadConfig();
	DataCluster getConfiguration();
	String getCodeName();
}
