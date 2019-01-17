package work.torp.jukeboxtiki.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.classes.Disc;
import work.torp.jukeboxtiki.classes.TikiJukebox;


public class BlockEvents implements Listener {
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt)
	{
		if (evt.getBlock() != null)
		{
			if (evt.getBlock().getType() == Material.JUKEBOX)
			{
				TikiJukebox jb = new TikiJukebox();
				jb.init();
				jb.setJukeboxID(Main.Jukeboxes.size() + 1);
				jb.setLocation(evt.getBlock().getLocation());
				jb.setOwner(evt.getPlayer().getUniqueId());
				jb.save();
				Main.Jukeboxes.put(evt.getBlock(), jb);
			}
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt)
	{
		if (evt.getBlock() != null)
		{
			if (evt.getBlock().getType() == Material.JUKEBOX)
			{
				if (Main.Jukeboxes.containsKey(evt.getBlock()))
				{
					TikiJukebox jb = Main.Jukeboxes.get(evt.getBlock());
					if (jb != null)
					{
						if (Main.getInstance().getInternalStorage())
						{
							if (jb.getDiscs() != null)
							{
								for (Disc d : jb.getDiscs())
								{
									// Eject each disc loaded into internal storage
									ItemStack isDisc = new ItemStack(d.getDisc(), 1);
									World world = evt.getBlock().getWorld();
									world.dropItem(evt.getBlock().getLocation(), isDisc);
								}
							}
						}
						if (jb.isDiscLoaded())
						{
							// Eject the currently loaded disc
							ItemStack isDisc = new ItemStack(jb.getLoadedDiscType(), 1);
							World world = evt.getBlock().getWorld();
							world.dropItem(evt.getBlock().getLocation(), isDisc);
						}
						Main.Jukeboxes.remove(evt.getBlock());
					}
				}
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockRedstoneEvent(BlockRedstoneEvent evt) {
		if (evt.getBlock() != null)
		{
			if (evt.getBlock().getType() == Material.JUKEBOX)
			{
				if (Main.Jukeboxes.containsKey(evt.getBlock()))
				{
					TikiJukebox jb = Main.Jukeboxes.get(evt.getBlock());
					if (jb != null)
					{
						if (evt.getBlock().isBlockPowered())
						{
							if (!jb.loadFirstDiscToStorage())
							{
								// unable to get disc
							}
						}
					}
				}
			}
		}
	}
}
