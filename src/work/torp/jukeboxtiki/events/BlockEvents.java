package work.torp.jukeboxtiki.events;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
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
				Alert.Player("Jukebox placed", evt.getPlayer(), true);
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
									Alert.DebugLog("BlockEvents", "onBlockBreak", ChatColor.RED + "Dropping: " + d.getDisc().name());
									// Eject each disc loaded into internal storage
									ItemStack isDisc = new ItemStack(d.getDisc(), 1);
									World world = evt.getBlock().getWorld();
									world.dropItem(evt.getBlock().getLocation(), isDisc);
								}
							}
						}
						
					}
				}
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockRedstoneEvent(BlockRedstoneEvent evt) {
		if (evt.getBlock() != null)
		{
		
			boolean foundJukebox = false;
			Block jbBlock = evt.getBlock();
			if (evt.getBlock().getRelative(BlockFace.NORTH).getType() == Material.JUKEBOX)
			{
				jbBlock = evt.getBlock().getRelative(BlockFace.NORTH);
				foundJukebox = true;
			}
			if (evt.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.JUKEBOX)
			{
				jbBlock = evt.getBlock().getRelative(BlockFace.SOUTH);
				foundJukebox = true;
			}
			if (evt.getBlock().getRelative(BlockFace.EAST).getType() == Material.JUKEBOX)
			{
				jbBlock = evt.getBlock().getRelative(BlockFace.EAST);
				foundJukebox = true;
			}
			if (evt.getBlock().getRelative(BlockFace.WEST).getType() == Material.JUKEBOX)
			{
				jbBlock = evt.getBlock().getRelative(BlockFace.WEST);
				foundJukebox = true;
			}
			if (evt.getBlock().getRelative(BlockFace.UP).getType() == Material.JUKEBOX)
			{
				jbBlock = evt.getBlock().getRelative(BlockFace.UP);
				foundJukebox = true;
			}
			if (evt.getBlock().getRelative(BlockFace.DOWN).getType() == Material.JUKEBOX)
			{
				jbBlock = evt.getBlock().getRelative(BlockFace.DOWN);
				foundJukebox = true;
			}
			
			if (foundJukebox)
			{
				
				if (jbBlock.getType() == Material.JUKEBOX)
				{
					if (Main.Jukeboxes.containsKey(jbBlock))
					{
						TikiJukebox jb = Main.Jukeboxes.get(jbBlock);
						if (jb != null)
						{
							if (evt.getBlock().getBlockPower() > 0)
							{
								if (!Main.NextSongButton.containsKey(jb))
								{
									Alert.DebugLog("BlockEvents", "onBlockRedstoneEvent", "Adding signal");
									Main.NextSongButton.put(jb, true);
								}
							}
						}
					}
				}	
			}
		}
	}
}
