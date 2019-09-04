package work.torp.jukeboxtiki.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.helpers.Check;

public class EntityEvents implements Listener {
	@EventHandler
	public void a(EntityDropItemEvent e)
	{
		Alert.DebugLog("EntityEvents", "EntityDropItemEvent", "Dropped!");
		//e.getItemDrop().getItemStack()
	}
	@EventHandler
	public void b(ItemSpawnEvent e)
	{
		boolean keepItem = false;
		//Alert.Log(e.getEventName().toString(), "");
		// ItemSpawnEvent is used when ejecting a disc from the Jukebox
		// rather than the BlockDispenseEvent - as we're using internal storage
		// we need to cancel this spawn event
		if (e.getEntity().getItemStack() != null) // check to make sure the item spawned is an ItemStack (not null)
		{
			//Alert.Log("ItemSpawnEvent", "Item stack is not null");
			if (Check.isMusicDisc(e.getEntity().getItemStack().getType())) // check to make sure the spawned ItemStack is a music disc
			{
				//Alert.Log("ItemSpawnEvent", "Item is a music disc");
				ItemMeta imeta = e.getEntity().getItemStack().getItemMeta();
				if (imeta != null) {
					//Alert.Log("ItemSpawnEvent", "Item meta is not null");
					if (imeta.getLore() != null) {
						//Alert.Log("ItemSpawnEvent", "Item lore is not null");
						for (String s : imeta.getLore()) {
							//Alert.Log("ItemSpawnEvent", s);
							if (s.equals("NOT JUKEBOX")) {
								imeta.setLore(new ArrayList<String>());
								e.getEntity().getItemStack().setItemMeta(imeta);
								keepItem = true;
							}
						} 
					} else {
						//Alert.Log("ItemSpawnEvent", "Item lore is null");
					}
				}
				
				if (!keepItem) {
					//Alert.Log("ItemSpawnEvent", "Cancelling event");
					e.setCancelled(true); // if it is a music disc, cancel the event
				} else {
					//Alert.Log("ItemSpawnEvent", "Keeping event");
				}
			}
		} else {
			//Alert.Log("ItemSpawnEvent", "itemstack is null");
		}
		
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
		if (evt.getEntity().getType() == EntityType.ITEM_FRAME)
		{
			//Alert.Log("EntityDamageByEntity", "item frame damage");
			if (evt.getEntity() instanceof ItemFrame) {
				ItemFrame i = ((ItemFrame)evt.getEntity());
	            ItemStack istack = i.getItem();
	            if (istack != null) {
		            if (Check.isMusicDisc(istack.getType())) 
		            {
			            ItemMeta imeta = istack.getItemMeta();
			            List<String> lore = new ArrayList<String>();
						lore.add("NOT JUKEBOX");
						imeta.setLore(lore);
						istack.setItemMeta(imeta);
						i.setItem(istack);
		            }
	            }
			}

		}
	}
}
