package com.volmit.phantom.imp.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.volmit.phantom.api.inventory.Element;
import com.volmit.phantom.api.inventory.ElementEvent;
import com.volmit.phantom.api.inventory.Window;
import com.volmit.phantom.api.inventory.WindowDecorator;
import com.volmit.phantom.api.inventory.WindowResolution;
import com.volmit.phantom.api.lang.Callback;
import com.volmit.phantom.api.lang.GMap;
import com.volmit.phantom.api.lang.GSet;
import com.volmit.phantom.api.math.M;
import com.volmit.phantom.api.sheduler.S;
import com.volmit.phantom.imp.plugin.PhantomPlugin;

public class UIWindow implements Window, Listener
{
	private WindowDecorator decorator;
	private final Player viewer;
	private Callback<Window> eClose;
	private WindowResolution resolution;
	private GMap<Integer, Element> elements;
	private String title;
	private boolean visible;
	private int viewportPosition;
	private int viewportSize;
	private int highestRow;
	private Inventory inventory;
	private int clickcheck;
	private boolean doubleclicked;
	private ItemStack[] cachedItems;

	public UIWindow(Player viewer)
	{
		clickcheck = 0;
		doubleclicked = false;
		this.viewer = viewer;
		this.elements = new GMap<>();
		setTitle("");
		setDecorator(new UIVoidDecorator());
		setResolution(WindowResolution.W9_H6);
		setViewportHeight(M.iclip(3, 1, getResolution().getMaxHeight()));
		setViewportPosition(0);
	}

	@EventHandler
	public void on(InventoryClickEvent e)
	{
		if(!e.getWhoClicked().equals(viewer))
		{
			return;
		}

		if(!isVisible())
		{
			return;
		}

		if(!viewer.getOpenInventory().getTopInventory().getName().equals(title))
		{
			return;
		}

		if(e.getClickedInventory() == null)
		{
			return;
		}

		if(!e.getView().getType().equals(getResolution().getType()))
		{
			return;
		}

		if(e.getClickedInventory().getType().equals(getResolution().getType()))
		{
			Element element = getElement(getLayoutPosition(e.getSlot()), getLayoutRow(e.getSlot()));

			switch(e.getAction())
			{
				case CLONE_STACK:
					break;
				case COLLECT_TO_CURSOR:
					break;
				case DROP_ALL_CURSOR:
					break;
				case DROP_ALL_SLOT:
					break;
				case DROP_ONE_CURSOR:
					break;
				case DROP_ONE_SLOT:
					break;
				case HOTBAR_MOVE_AND_READD:
					break;
				case HOTBAR_SWAP:
					break;
				case MOVE_TO_OTHER_INVENTORY:
					break;
				case NOTHING:
					break;
				case PICKUP_ALL:
					break;
				case PICKUP_HALF:
					break;
				case PICKUP_ONE:
					break;
				case PICKUP_SOME:
					break;
				case PLACE_ALL:
					break;
				case PLACE_ONE:
					break;
				case PLACE_SOME:
					break;
				case SWAP_WITH_CURSOR:
					break;
				case UNKNOWN:
					break;
			}

			switch(e.getClick())
			{
				case CONTROL_DROP:
					break;
				case CREATIVE:
					break;
				case DOUBLE_CLICK:
					doubleclicked = true;
					break;
				case DROP:
					break;
				case LEFT:

					clickcheck++;

					if(clickcheck == 1)
					{
						new S()
						{
							@Override
							public void run()
							{
								if(clickcheck == 1)
								{
									clickcheck = 0;

									if(element != null)
									{
										element.call(ElementEvent.LEFT, element);
									}
								}
							}
						};
					}

					else if(clickcheck == 2)
					{
						new S()
						{
							@Override
							public void run()
							{
								if(doubleclicked)
								{
									doubleclicked = false;
								}

								else
								{
									scroll(1);
								}

								clickcheck = 0;
							}
						};
					}

					break;
				case MIDDLE:
					break;
				case NUMBER_KEY:
					break;
				case RIGHT:
					if(element != null)
					{
						element.call(ElementEvent.RIGHT, element);
					}

					else
					{
						scroll(-1);
					}
					break;
				case SHIFT_LEFT:
					if(element != null)
					{
						element.call(ElementEvent.SHIFT_LEFT, element);
					}
					break;
				case SHIFT_RIGHT:
					if(element != null)
					{
						element.call(ElementEvent.SHIFT_RIGHT, element);
					}
					break;
				case WINDOW_BORDER_LEFT:
					break;
				case WINDOW_BORDER_RIGHT:
					break;
				case UNKNOWN:
					break;
			}
		}

		e.setCancelled(true);
	}

	@EventHandler
	public void on(InventoryCloseEvent e)
	{
		if(!e.getPlayer().equals(viewer))
		{
			return;
		}

		if(!e.getPlayer().getOpenInventory().getTopInventory().getName().equals(title))
		{
			return;
		}

		if(isVisible())
		{
			close();
			callClosed();
		}
	}

	@Override
	public UIWindow setDecorator(WindowDecorator decorator)
	{
		this.decorator = decorator;
		return this;
	}

	@Override
	public WindowDecorator getDecorator()
	{
		return decorator;
	}

	@Override
	public UIWindow close()
	{
		setVisible(false);
		return this;
	}

	@Override
	public UIWindow open()
	{
		setVisible(true);
		return this;
	}

	@Override
	public UIWindow setVisible(boolean visible)
	{
		if(isVisible() == visible)
		{
			return this;
		}

		if(visible)
		{
			Bukkit.getPluginManager().registerEvents(this, PhantomPlugin.plugin);

			if(getResolution().getType().equals(InventoryType.CHEST))
			{
				inventory = Bukkit.createInventory(null, getViewportHeight() * 9, getTitle());
			}

			else
			{
				inventory = Bukkit.createInventory(null, getResolution().getType(), getTitle());
			}

			cacheItems();
			viewer.openInventory(inventory);
			this.visible = visible;
			updateInventory();
		}

		else
		{
			this.visible = visible;
			HandlerList.unregisterAll(this);
			viewer.closeInventory();
			restoreItems();
		}

		this.visible = visible;
		return this;
	}

	private void restoreItems()
	{
		for(int i = 0; i < viewer.getInventory().getSize(); i++)
		{
			viewer.getInventory().clear(i);
			ItemStack iv = cachedItems[i];

			if(iv != null)
			{
				viewer.getInventory().setItem(i, iv);
			}
		}
	}

	private void cacheItems()
	{
		cachedItems = new ItemStack[viewer.getInventory().getSize()];

		for(int i = 0; i < viewer.getInventory().getSize(); i++)
		{
			ItemStack iv = viewer.getInventory().getItem(i);
			cachedItems[i] = iv != null ? iv.clone() : iv;
			viewer.getInventory().clear(i);
		}
	}

	@Override
	public boolean isVisible()
	{
		return visible;
	}

	@Override
	public int getViewportPosition()
	{
		return viewportPosition;
	}

	@Override
	public UIWindow setViewportPosition(int viewportPosition)
	{
		this.viewportPosition = viewportPosition;
		scroll(0);
		updateInventory();

		return this;
	}

	@Override
	public int getMaxViewportPosition()
	{
		return Math.max(0, highestRow - getViewportHeight());
	}

	@Override
	public UIWindow scroll(int direction)
	{
		viewportPosition = (int) M.clip(viewportPosition + direction, 0, getMaxViewportPosition());
		updateInventory();

		return this;
	}

	@Override
	public int getViewportHeight()
	{
		return viewportSize;
	}

	@Override
	public UIWindow setViewportHeight(int height)
	{
		viewportSize = M.iclip(height, 1, getResolution().getMaxHeight());

		if(isVisible())
		{
			reopen();
		}

		return this;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public UIWindow setTitle(String title)
	{
		this.title = title;

		if(isVisible())
		{
			reopen();
		}

		return this;
	}

	@Override
	public UIWindow setElement(int position, int row, Element e)
	{
		if(row > highestRow)
		{
			highestRow = row;
		}

		elements.put(getRealPosition(M.iclip(position, -getResolution().getMaxWidthOffset(), getResolution().getMaxWidthOffset()), row), e);
		updateInventory();
		return this;
	}

	@Override
	public Element getElement(int position, int row)
	{
		return elements.get(getRealPosition(M.iclip(position, -getResolution().getMaxWidthOffset(), getResolution().getMaxWidthOffset()), row));
	}

	@Override
	public Player getViewer()
	{
		return viewer;
	}

	@Override
	public UIWindow onClosed(Callback<Window> window)
	{
		eClose = window;
		return this;
	}

	@Override
	public int getViewportSlots()
	{
		return getViewportHeight() * getResolution().getWidth();
	}

	@Override
	public int getLayoutRow(int viewportSlottedPosition)
	{
		return getRow(getRealLayoutPosition(viewportSlottedPosition));
	}

	@Override
	public int getLayoutPosition(int viewportSlottedPosition)
	{
		return getPosition(viewportSlottedPosition);
	}

	@Override
	public int getRealLayoutPosition(int viewportSlottedPosition)
	{
		return getRealPosition(getPosition(viewportSlottedPosition), getRow(viewportSlottedPosition) + getViewportPosition());
	}

	@Override
	public int getRealPosition(int position, int row)
	{
		return ((row * getResolution().getWidth()) + getResolution().getMaxWidthOffset()) + M.iclip(position, -getResolution().getMaxWidthOffset(), getResolution().getMaxWidthOffset());
	}

	@Override
	public int getRow(int realPosition)
	{
		return realPosition / getResolution().getWidth();
	}

	@Override
	public int getPosition(int realPosition)
	{
		return (realPosition % getResolution().getWidth()) - getResolution().getMaxWidthOffset();
	}

	@Override
	public Window callClosed()
	{
		if(eClose != null)
		{
			eClose.run(this);
		}

		return this;
	}

	@Override
	public boolean hasElement(int position, int row)
	{
		return getElement(position, row) != null;
	}

	@Override
	public WindowResolution getResolution()
	{
		return resolution;
	}

	@Override
	public Window setResolution(WindowResolution resolution)
	{
		close();
		this.resolution = resolution;
		setViewportHeight(M.iclip(getViewportHeight(), 1, getResolution().getMaxHeight()));
		return this;
	}

	@Override
	public Window clearElements()
	{
		highestRow = 0;
		elements.clear();
		updateInventory();
		return this;
	}

	@Override
	public Window updateInventory()
	{
		if(isVisible())
		{
			ItemStack[] is = inventory.getStorageContents();
			GSet<ItemStack> isf = new GSet<ItemStack>();

			for(int i = 0; i < is.length; i++)
			{
				ItemStack isc = is[i];
				ItemStack isx = computeItemStack(i);
				int layoutRow = getLayoutRow(i);
				int layoutPosition = getLayoutPosition(i);

				if(isx != null && !hasElement(layoutPosition, layoutRow))
				{
					ItemStack gg = isx.clone();
					gg.setAmount(gg.getAmount() + 1);
					isf.add(gg);
				}

				if(((isc == null) != (isx == null)) || isx != null && isc != null && !isc.equals(isx))
				{
					inventory.setItem(i, isx);
				}
			}

			viewer.getInventory().clear();

			for(ItemStack i : isf)
			{
				viewer.getInventory().addItem(i);
			}
		}

		return this;
	}

	@Override
	public ItemStack computeItemStack(int viewportSlot)
	{
		int layoutRow = getLayoutRow(viewportSlot);
		int layoutPosition = getLayoutPosition(viewportSlot);
		Element e = hasElement(layoutPosition, layoutRow) ? getElement(layoutPosition, layoutRow) : getDecorator().onDecorateBackground(this, layoutPosition, layoutRow);

		if(e != null)
		{
			return e.computeItemStack();
		}

		return null;
	}

	@Override
	public Window reopen()
	{
		return this.close().open();
	}

	@Override
	public ItemStack[] getPlayerCachedItems()
	{
		return cachedItems;
	}
}
