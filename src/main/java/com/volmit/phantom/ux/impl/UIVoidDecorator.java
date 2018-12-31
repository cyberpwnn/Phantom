package com.volmit.phantom.ux.impl;

import com.volmit.phantom.ux.Element;
import com.volmit.phantom.ux.Window;
import com.volmit.phantom.ux.WindowDecorator;

public class UIVoidDecorator implements WindowDecorator
{
	@Override
	public Element onDecorateBackground(Window window, int position, int row)
	{
		return null;
	}
}

