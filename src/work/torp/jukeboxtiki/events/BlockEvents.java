package work.torp.jukeboxtiki.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.JukeboxBlock;
import work.torp.jukeboxtiki.classes.MusicDisc;
import work.torp.jukeboxtiki.helpers.Check;
import work.torp.jukeboxtiki.helpers.Convert;

public class BlockEvents implements Listener {
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent evt)
	{
		if (evt.getBlock() != null) // check to make sure the block is not null (shouldn't happen)
		{
			if (evt.getBlock().getType() == Material.JUKEBOX) // check to make sure the block placed was a Jukebox
			{
				if (Check.hasPermission(evt.getPlayer(), "jukebox.place"))
				{
					JukeboxBlock jbb = new JukeboxBlock(); // create a new JukeboxBlock
					
					List<String> lstStocked = Main.getInstance().getConfig().getStringList("internal_prestocked");
					List<MusicDisc> lstMD = new ArrayList<MusicDisc>();
					if (lstStocked != null)
					{
						int iOrdBy = 0;
						for (String discname : lstStocked)
						{
							Material m = Convert.StringToMaterial(discname);
							if (m != null)
							{
								if (Check.isMusicDisc(m))
								{
									MusicDisc md = new MusicDisc();
									md.init();
									md.setDisc(m);
									md.setOrderBy(iOrdBy);
									lstMD.add(md);
								}
							}
							iOrdBy++;
						}
					}
					jbb.init(evt.getPlayer().getUniqueId(), evt.getBlock().getLocation(), lstMD); // initialize the JukeboxBlock
					Alert.Player("Jukebox placed", evt.getPlayer(), true);
				}
			}
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent evt)
	{
		if (evt.getBlock() != null) // check to make sure the block is not null (shouldn't happen)
		{
			if (evt.getBlock().getType() == Material.JUKEBOX) // check to make sure the block broken was a Jukebox
			{
				if (Main.JukeboxBlocks.containsKey(evt.getBlock())) // check if the block broken was a JukeboxBlock
				{
					Alert.Player("You cannot break this Jukebox.  Please open the control panel and select Remove Jukebox", evt.getPlayer(), true);
					evt.setCancelled(true);
					return;
//					JukeboxBlock jbb = Main.JukeboxBlocks.get(evt.getBlock()); // get the JukeboxBlock
//					if (jbb != null) // check to make sure the JukeboxBlock is not null (shouldn't happen)
//					{
//						if (Check.hasPermission(evt.getPlayer(), "jukebox.admin") || jbb.getOwner().equals(evt.getPlayer().getUniqueId()))
//						{
//							Alert.DebugLog("BlockEvents", "onBlockBreak", "JukeboxBlock is being broken");
//							if (jbb.getInternalStorage() != null) // check to make sure the internal storage isn't null (shouldn't happen after init())
//							{
//								Alert.DebugLog("BlockEvents", "onBlockBreak", "JukeboxBlock storage is not null");
//								if (jbb.getInternalStorage().isEmpty() || jbb.getInternalStorage().size() == 0)
//								{
//									evt.setCancelled(true);
//									Alert.DebugLog("BlockEvents", "onBlockBreak", "JukeboxBlock is empty");
//									jbb.close(); // close our JukeboxBlock (removes it from HashMap and file/database)
//									
//									jbb.getLocation().getBlock().setType(Material.AIR);
//									Alert.Player("Jukebox broken", evt.getPlayer(), true);
//									return;
//									
//								} else {
//									Alert.DebugLog("BlockEvents", "onBlockBreak", "JukeboxBlock is not empty - cancel break");
//									evt.setCancelled(true);
//									Alert.Player("You must remove the music discs from the Jukebox before breaking it", evt.getPlayer(), true);
//									return;
//								}
//							}
//						} else {
//							Alert.Player("You do not have permission to break this Jukebox", evt.getPlayer(), true);
//						}
//					}
				}
			}
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onBlockRedstoneEvent(BlockRedstoneEvent evt) {
		if (evt.getBlock() != null) // check to make sure the block is not null
		{
			//Alert.DebugLog("BlockEvents", "onBlockRedstone", "Block = " + Convert.LocationToReadableString(evt.getBlock().getLocation()));

			
			if (evt.getNewCurrent() == 0 && evt.getOldCurrent() > 0) {
				
				Alert.DebugLog("BlockEvents", "onBlockRedstone", "old current = " + Integer.toString(evt.getOldCurrent()) + " new current = " + Integer.toString(evt.getNewCurrent()));
				
				boolean foundJukebox = false; // create a boolean to record if the jukebox was found 
				Block jbBlock = evt.getBlock(); // create the block in its own variable
				if (evt.getBlock().getRelative(BlockFace.NORTH).getType() == Material.JUKEBOX) // check the north face to see if there is a Jukebox there
				{
					jbBlock = evt.getBlock().getRelative(BlockFace.NORTH); // set the block
					foundJukebox = true; // mark as found
				}
				if (evt.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.JUKEBOX) // check the south face to see if there is a Jukebox there
				{
					jbBlock = evt.getBlock().getRelative(BlockFace.SOUTH); // set the block
					foundJukebox = true; // mark as found
				}
				if (evt.getBlock().getRelative(BlockFace.EAST).getType() == Material.JUKEBOX) // check the east face to see if there is a Jukebox there
				{
					jbBlock = evt.getBlock().getRelative(BlockFace.EAST); // set the block
					foundJukebox = true; // mark as found
				}
				if (evt.getBlock().getRelative(BlockFace.WEST).getType() == Material.JUKEBOX) // check the west face to see if there is a Jukebox there
				{
					jbBlock = evt.getBlock().getRelative(BlockFace.WEST); // set the block
					foundJukebox = true; // mark as found
				}
				if (evt.getBlock().getRelative(BlockFace.UP).getType() == Material.JUKEBOX) // check the top to see if there is a Jukebox there
				{
					jbBlock = evt.getBlock().getRelative(BlockFace.UP); // set the block
					foundJukebox = true; // mark as found
				}
				if (evt.getBlock().getRelative(BlockFace.DOWN).getType() == Material.JUKEBOX) // check the bottom to see if there is a Jukebox there
				{
					jbBlock = evt.getBlock().getRelative(BlockFace.DOWN); // set the block
					foundJukebox = true; // mark as found
				}
				
				if (foundJukebox) // if there was a Jukebox found, continue
				{
					
					if (jbBlock.getType() == Material.JUKEBOX) // check to make sure it is a Jukebox (redundant - we know it is from above)
					{
						if (Main.JukeboxBlocks.containsKey(jbBlock)) // checking to see if our Jukebox is a JukeboxBlock
						{
							JukeboxBlock jbb = Main.JukeboxBlocks.get(jbBlock); // get the JukeboxBlock
							if (jbb != null) // check to make sure the JukeboxBlock is not null (shouldn't happen)
							{
								if (evt.getBlock().getBlockPower() > 0) // check to see if fthe event block has power
								{
									if (!Main.NextSong.containsKey(jbb)) // check that we don't already have the next song queued
										{
											Alert.DebugLog("BlockEvents", "onBlockRedstoneEvent", "Adding signal");
											Main.NextSong.put(jbb, true); // queue the next song
										}
								}	
							}
						}
					}	
				}
			}
			
		}
	}
}
