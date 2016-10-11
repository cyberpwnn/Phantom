package org.phantomapi.currency;

import org.bukkit.entity.Player;

/**
 * Transaction util
 * 
 * @author cyberpwn
 */
public class Transaction
{
	private Player from;
	private Player to;
	private Currency currency;
	private CurrencyMessager messager;
	private Double amount;
	
	/**
	 * Create a transaction
	 * 
	 * @param currency
	 *            the currency
	 */
	public Transaction(Currency currency)
	{
		from = null;
		to = null;
		amount = null;
		this.currency = currency;
	}
	
	/**
	 * Note that currency is coming FROM a player
	 * 
	 * @param from
	 *            the from player
	 * @return the transaction (chain)
	 */
	public Transaction from(Player from)
	{
		this.from = from;
		return this;
	}
	
	/**
	 * Note that currency is going TO a player
	 * 
	 * @param to
	 *            the to player
	 * @return the transaction (chain)
	 */
	public Transaction to(Player to)
	{
		this.to = to;
		return this;
	}
	
	/**
	 * Note the amount of the transaction
	 * 
	 * @param amount
	 *            the amount
	 * @return the transaction (chain)
	 */
	public Transaction amount(Double amount)
	{
		this.amount = amount;
		return this;
	}
	
	/**
	 * Commit the transaction
	 */
	public void commit()
	{
		if(amount != null)
		{
			if(from != null)
			{
				double currentFrom = currency.get(from);
				
				if(to != null)
				{
					double currentTo = currency.get(to);
					
					transfer();
					diff(from, currentFrom, currency.get(from));
					diff(to, currentTo, currency.get(to));
				}
				
				else
				{
					take();
					diff(from, currentFrom, currency.get(from));
				}
			}
			
			if(to != null)
			{
				double currentTo = currency.get(to);
				
				if(from != null)
				{
					double currentFrom = currency.get(from);
					
					transfer();
					diff(from, currentFrom, currency.get(from));
					diff(to, currentTo, currency.get(to));
				}
				
				else
				{
					give();
					diff(to, currentTo, currency.get(to));
				}
			}
		}
	}
	
	private void diff(Player p, double f, double t)
	{
		if(f > t)
		{
			messager.spent(p, f - t, t);
		}
		
		else
		{
			messager.earned(p, t - f, t);
		}
	}
	
	private void give()
	{
		if(to != null && amount != null)
		{
			currency.give(to, amount);
		}
	}
	
	private void take()
	{
		if(from != null && amount != null)
		{
			currency.take(from, amount);
		}
	}
	
	private void transfer()
	{
		if(to != null && from != null && amount != null)
		{
			give();
			take();
		}
	}
}
