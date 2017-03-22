package org.phantomapi.nbt;

import java.util.ArrayList;
import java.util.List;

public class VillagerNBT extends BreedNBT
{
	
	private ArrayList<VillagerNBTOffer> _offers;
	
	static
	{
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Villager");
		variables.add("Career", new VillagerCareerVariable());
		variables.add("CareerLevel", new IntegerVariable("CareerLevel", 0));
		variables.add("Willing", new BooleanVariable("Willing")); // XXX: not
																	// effect?
		// TODO: add villager inventory
		registerVariables(VillagerNBT.class, variables);
	}
	
	public void clearOffers()
	{
		_data.remove("Offers");
		_offers = null;
	}
	
	public void addOffer(VillagerNBTOffer offer)
	{
		NBTTagCompound offers = _data.getCompound("Offers");
		if(offers == null)
		{
			offers = new NBTTagCompound();
			_data.setCompound("Offers", offers);
		}
		NBTTagList recipes = offers.getList("Recipes");
		if(recipes == null)
		{
			recipes = new NBTTagList();
			offers.setList("Recipes", recipes);
		}
		recipes.add(offer.getCompound());
		if(_offers != null)
		{
			_offers.add(offer);
		}
	}
	
	public List<VillagerNBTOffer> getOffers()
	{
		if(_offers == null)
		{
			_offers = new ArrayList<VillagerNBTOffer>();
			if(_data.hasKey("Offers"))
			{
				NBTTagCompound offers = _data.getCompound("Offers");
				if(offers.hasKey("Recipes"))
				{
					Object[] recipes = offers.getListAsArray("Recipes");
					for(Object recipe : recipes)
					{
						_offers.add(new VillagerNBTOffer((NBTTagCompound) recipe));
					}
				}
			}
		}
		return _offers;
	}
	
}
