package com.volmit.phantom.imp.inventory;

import com.volmit.phantom.api.inventory.Element;
import com.volmit.phantom.api.inventory.Window;
import com.volmit.phantom.api.inventory.WindowDecorator;

public class UIVoidDecorator implements WindowDecorator
{
	@Override
	public Element onDecorateBackground(Window window, int position, int row)
	{
		return null;
	}
}

