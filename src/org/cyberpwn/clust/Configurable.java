package org.cyberpwn.clust;

public interface Configurable
{
	void onNewConfig();
	void onReadConfig();
	DataCluster getConfiguration();
	String getCodeName();
}
