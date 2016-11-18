package org.phantomapi.graph;

import java.awt.Color;
import javax.script.ScriptException;
import org.phantomapi.util.M;

public class EquationGraph extends PhantomGraph
{
	private String expression;
	private double scaleX;
	private double scaleY;
	private double shiftX;
	private double shiftY;
	
	public EquationGraph(String expression)
	{
		this.expression = expression;
		scaleX = 1;
		scaleY = 1;
		shiftX = 0;
		shiftY = 0;
	}
	
	@Override
	public void renderGraph(GraphHolder holder)
	{
		for(int x = (int) shiftX; x < holder.getGraphWidth(); x++)
		{
			try
			{
				double y = (M.evaluate(expression.replaceAll("x", (x * scaleX) + "")) * scaleY) + shiftY;
				
				if(y < holder.getGraphHeight() - 1 && y > 0)
				{
					holder.drawGraph(x, (int) y, Color.RED);
				}
			}
			
			catch(ScriptException e)
			{
				
			}
		}
	}
	
	public String getExpression()
	{
		return expression;
	}
	
	public double getScaleX()
	{
		return scaleX;
	}
	
	public double getScaleY()
	{
		return scaleY;
	}
	
	public double getShiftX()
	{
		return shiftX;
	}
	
	public void setShiftX(double shiftX)
	{
		this.shiftX = shiftX;
	}
	
	public double getShiftY()
	{
		return shiftY;
	}
	
	public void setShiftY(double shiftY)
	{
		this.shiftY = shiftY;
	}
	
	public void setExpression(String expression)
	{
		this.expression = expression;
	}
	
	public void setScaleX(double scaleX)
	{
		this.scaleX = scaleX;
	}
	
	public void setScaleY(double scaleY)
	{
		this.scaleY = scaleY;
	}
}
