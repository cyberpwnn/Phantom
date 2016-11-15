package org.phantomapi.blockmeta;

import java.io.IOException;
import org.phantomapi.clust.ConfigurableObject;
import org.phantomapi.clust.DataEntity;

public class MetaObject extends ConfigurableObject implements DataEntity
{
	public MetaObject(String codeName)
	{
		super(codeName);
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		return getConfiguration().compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		getConfiguration().clear();
		getConfiguration().addCompressed(data);
	}
}
