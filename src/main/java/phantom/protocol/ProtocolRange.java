package phantom.protocol;

public class ProtocolRange
{
	private Protocol from;
	private Protocol to;

	public ProtocolRange(Protocol from, Protocol to)
	{
		this.from = from;
		this.to = to;
	}

	public Protocol getFrom()
	{
		return from;
	}

	public Protocol getTo()
	{
		return to;
	}

	public boolean contains(Protocol p)
	{
		return p.getMetaVersion() >= from.getMetaVersion() && p.getMetaVersion() <= to.getMetaVersion();
	}

	@Override
	public String toString()
	{
		return from.getVersionString() + " - " + to.getVersionString();
	}
}
