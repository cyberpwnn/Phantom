package phantom.pluginadapter;

public enum PluginType
{
	FAWE("FastAsyncWorldEdit"),
	VAULT("Vault"),
	PROTOCOLLIB("ProtocolLib");

	private String name;

	private PluginType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
