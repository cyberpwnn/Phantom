package com.volmit.phantom.api.intercom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import com.volmit.phantom.util.net.FastSerializer;

public class IntercomMessage<M extends Serializable>
{
	private UUID payload;
	private String server;
	private M message;

	public IntercomMessage(String server, M message)
	{
		this.message = message;
		this.payload = UUID.randomUUID();
		this.server = server;
	}

	public void payload(UUID p)
	{
		payload = p;
	}

	public IntercomMessage(M message)
	{
		this.message = message;
		this.payload = UUID.randomUUID();
		this.server = "";
	}

	public IntercomMessage()
	{
		this.message = null;
		this.payload = UUID.randomUUID();
		this.server = "";
	}

	@SuppressWarnings("unchecked")
	public IntercomMessage(Serializable message, boolean force)
	{
		this.message = (M) message;
		this.payload = UUID.randomUUID();
		this.server = "";
	}

	public IntercomMessage(String server, M message, IntercomMessage<?> respondingTo)
	{
		this(server, message);
		this.payload = respondingTo.payload;
	}

	public void asResponseTo(IntercomMessage<?> f)
	{
		payload = f.payload;
	}

	public UUID getPayload()
	{
		return payload;
	}

	public String getServer()
	{
		return server;
	}

	public M getMessage()
	{
		return message;
	}

	public void read(DataInputStream din) throws IOException
	{
		payload = UUID.fromString(din.readUTF());
		server = din.readUTF();
		byte[] b = new byte[din.readInt()];
		din.read(b);
		message = FastSerializer.deserialize(b);
	}

	public void write(DataOutputStream dos) throws IOException
	{
		dos.writeUTF(payload.toString());
		dos.writeUTF(server);
		byte[] b = FastSerializer.serialize(getMessage());
		dos.writeInt(b.length);
		dos.write(b);
	}
}
