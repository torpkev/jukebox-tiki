package work.torp.jukeboxtiki.events;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.Main.IGUI;
import work.torp.jukeboxtiki.alerts.Alert;
//import work.torp.jukeboxtiki.classes.Disc;
import work.torp.jukeboxtiki.classes.JukeboxBlock;
import work.torp.jukeboxtiki.classes.MusicDisc;
//import work.torp.jukeboxtiki.classes.TikiJukebox;
import work.torp.jukeboxtiki.helpers.Check;

public class InventoryEvents implements Listener {
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {  
        if(e.getInventory().getHolder() instanceof IGUI) // check if the inventory opened is our storage GUI
        { 
        	Player player = null; // create the Player object
        	if (e.getWhoClicked() instanceof Player) // check to make sure the HumanEntity that clicked it is a player (should always be so)
        	{
        		player = (Player) e.getWhoClicked(); // cast the HumanEntity to Player
        		if (Main.StorageOpen.containsKey(player.getUniqueId())) // Check to see if we've got an open storage (we always should)
        		{
        			JukeboxBlock jbb = Main.StorageOpen.get(player.getUniqueId()); // get the JukeboxBlock that is open for storage
        			if (jbb != null) // check to make sure our JukeboxBlock is not null (this should never be null)
        			{
        				IGUI gui = (IGUI) e.getInventory().getHolder(); // get the InventoryHolder
        				gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem()); // send the info to the GUI class 
        			}
        		} else {
        			if (ChatColor.stripColor(e.getInventory().getName()).equals(ChatColor.stripColor("Jukebox Controls"))) // check if the inventory being closed is our Jukebox Storage
        			{
        				Alert.DebugLog("InventoryEvents", "onInventoryClick", "Click in Jukebox Controls");
	        			if (Main.ControlsOpen.containsKey(player.getUniqueId())) // Check to see if we've got an open storage (we always should)
	            		{
	        				JukeboxBlock jbb = Main.ControlsOpen.get(player.getUniqueId()); // get the JukeboxBlock that is open for storage
	            			if (jbb != null) // check to make sure our JukeboxBlock is not null (this should never be null)
	            			{
	            				if (e.getRawSlot() == 3) // stop
	            				{
	            					jbb.stop();
	            				}
	            				if (e.getRawSlot() == 5) // play
	            				{
	            					Main.NextSong.put(jbb, true);
	            				}
	            				IGUI gui = (IGUI) e.getInventory().getHolder(); // get the InventoryHolder
	            				gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem()); // send the info to the GUI class 
	            			}
	            		}
	        			e.setCancelled(true);
        			}
        		}
        	}  
        }      
    }
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Player player = null; // create the Player object
    	if (e.getPlayer() instanceof Player) // check to make sure the HumanEntity that clicked it is a player (should always be so)
    	{
    		player = (Player) e.getPlayer();
    	}
		if (ChatColor.stripColor(e.getInventory().getName()).equals(ChatColor.stripColor("Jukebox Storage"))) // check if the inventory being closed is our Jukebox Storage
		{
			if (Main.StorageOpen.containsKey(e.getPlayer().getUniqueId())) // check if we've got an open storage (we always should)
			{
				JukeboxBlock jbb = Main.StorageOpen.get(e.getPlayer().getUniqueId()); // get the JukeboxBlock
				if (jbb != null) // check to make sure the JukeboxBlock is not null (it should never be null)
				{
					List<MusicDisc> lstMD = new ArrayList<MusicDisc>(); // create a new list to hold MusicDisc records
					int iOrdBy = 0; // create an integer to hold our storage position which will become our orderby
        			for (ItemStack istack : e.getInventory()) // loop through the ItemStack values in the inventory
        	    	{
        				if (istack != null) // check to make sure the ItemStack is not null (it could be)
        	    		{
        					if (Check.isMusicDisc(istack.getType())) // check to make sure the ItemStack is a music disc
        					{
        						MusicDisc md = new MusicDisc(); // create a new MusicDisc object
        	        			md.init(); // initialize the MusicDisc
        	        			md.set(istack.getType(), iOrdBy); // set the disc and order by (name & song length are automatically populated) 
        	        			lstMD.add(md); // add the MusicDisc to the list
        	        			iOrdBy++; // Add to the orderby integer
        					} else {
        						// if it is not a music disc that was stored in the internal storage, drop it back out
        						World world = e.getPlayer().getLocation().getWorld();  // get the world
        						world.dropItem(e.getPlayer().getLocation(), istack); // drop the item
        					}
        	    		}
        	    	}
        			Alert.DebugLog("InventoryEvents", "onInventoryClose", "Saving inventory to hashmap");
        			jbb.setInternalStorage(lstMD);
        			Main.JukeboxBlocks.put(jbb.getLocation().getBlock(), jbb);
        			Main.StorageOpen.remove(e.getPlayer().getUniqueId());
				}
			}
		}
		if (ChatColor.stripColor(e.getInventory().getName()).equals(ChatColor.stripColor("Jukebox Controls"))) // check if the inventory being closed is our Jukebox Storage
		{
			Alert.DebugLog("InventoryEvents", "onInventoryClose", "Jukebox Controls closed");
			if (Main.ControlsOpen.containsKey(e.getPlayer().getUniqueId())) // check if we've got an open storage (we always should)
			{
				JukeboxBlock jbb = Main.ControlsOpen.get(e.getPlayer().getUniqueId()); // get the JukeboxBlock
				if (jbb != null) // check to make sure the JukeboxBlock is not null (it should never be null)
				{
					Alert.DebugLog("InventoryEvents", "onInventoryClose", "Controls closed");
					Main.ControlsOpen.remove(e.getPlayer().getUniqueId());
					HashMap<UUID, ItemStack[]> hmInv = Main.PlayerInventory;
					if (hmInv != null)
					{
						Alert.Log("InventoryTest", "Getting player inventory back from hashmap");
						ItemStack[] inv = hmInv.get(e.getPlayer().getUniqueId());
						if (inv != null)
						{
							int j = 0;
							for (ItemStack i : inv)
							{
								if (i != null)
								{
									//Alert.Log("GUI Restore: ", "Setting " + i.toString() + " to position " + Integer.toString(j));
									e.getPlayer().getInventory().setItem(j, i);
								} else {
									//Alert.Log("GUI Restore: ", "ItemStack is null - Setting AIR to position " + Integer.toString(j));
									e.getPlayer().getInventory().setItem(j, new ItemStack(Material.AIR, 1));
								}
								j++;
							}
							if (player != null)
							{
								player.updateInventory();
							}
						    Main.PlayerInventory.remove(e.getPlayer().getUniqueId());
						} else {
							Alert.Log("InventoryTest", "No such entry in hashmap");
						}
					}
				}
			}
		}
	}
}
