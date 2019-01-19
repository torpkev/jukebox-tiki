package work.torp.jukeboxtiki.events;

import java.util.HashSet;
import java.util.Set;

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
import work.torp.jukeboxtiki.classes.Disc;
import work.torp.jukeboxtiki.classes.TikiJukebox;
import work.torp.jukeboxtiki.helpers.Check;

public class InventoryEvents implements Listener {
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {  
        if(e.getInventory().getHolder() instanceof IGUI) {

        	Player player = null;

        	if (e.getWhoClicked() instanceof Player)
        	{
        		player = (Player) e.getWhoClicked();
        	
        		if (Main.OpenStorage.containsKey(player.getUniqueId()))
            	{
            		TikiJukebox tjb = Main.OpenStorage.get(player.getUniqueId());
            		if (tjb != null)
            		{
                        IGUI gui = (IGUI) e.getInventory().getHolder();
                        gui.onGUIClick((Player)e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem());
            		}
            	}
        	}  
        }      
    }
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Alert.Log("INV", ChatColor.GREEN + e.getInventory().getName());
		if (ChatColor.stripColor(e.getInventory().getName()).equals(ChatColor.stripColor("Jukebox Storage")))
		{
			Alert.Log("INV", ChatColor.GREEN + "Checking OpenStorage");
			if (Main.OpenStorage.containsKey(e.getPlayer().getUniqueId()))
        	{
        		TikiJukebox tjb = Main.OpenStorage.get(e.getPlayer().getUniqueId());
        		if (tjb != null)
        		{
        			Alert.Log("INV", ChatColor.GREEN + "Got Jukebox");
        			Set<Disc> lstD = new HashSet<Disc>();
        			int i = 0;
        			for (ItemStack istack : e.getInventory())
        	    	{
        				if (istack != null)
        	    		{
        					Alert.Log("INV", ChatColor.GOLD + "Contains: " + istack.getType().name());
        					if (Check.isMusicDisc(istack.getType()))
        					{
        						Disc d = new Disc();
        	        			d.init();
        	        			d.set(istack.getType(), i);
        	        			lstD.add(d);
        	        			i++;
        					} else {
        						World world = e.getPlayer().getLocation().getWorld();
        						world.dropItem(e.getPlayer().getLocation(), istack);
        					}
        	    		}
        	    	}
        			tjb.setDiscs(lstD);
        			Main.Jukeboxes.put(tjb.getBlock(), tjb);
        			Main.NextSongButton.put(tjb,  true);
        		}
        	}
		}
	}
    
//	@EventHandler
//	public void onInventoryMoveItem(InventoryDragEvent e)
//	{
//		if (e.getItem() != null)
//		{
//			Alert.DebugLog("InventoryEvents",  "onInventoryMoveItem", "Item: " + e.getItem().getType().name());
//			Alert.DebugLog("InventoryEvents",  "onInventoryMoveItem", "FromInventory: " + e.getSource().getName());
//			Alert.DebugLog("InventoryEvents",  "onInventoryMoveItem", "ToInventory: " + e.getDestination().getName());
//		}
//	}
}
