package work.torp.jukeboxtiki.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.StorageGUI;
import work.torp.jukeboxtiki.classes.ControlsGUI;
import work.torp.jukeboxtiki.classes.JukeboxBlock;

public class PlayerEvents implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt)
	{
		if (evt.getClickedBlock() != null) // check to make sure a block was clicked
		{
			if (evt.getClickedBlock().getType().equals(Material.JUKEBOX)) // check to make sure the clicked block was a Jukebox
			{
				if (evt.getAction() == Action.RIGHT_CLICK_BLOCK) // check to make sure it was a right click
				{
					if (evt.getHand() != null) // check to make sure we used a hand
					{
						if (evt.getHand() == EquipmentSlot.HAND) // check to make sure it was the main hand
						{	
							if (Main.JukeboxBlocks.containsKey(evt.getClickedBlock())) // check to see if the Jukebox clicked is a JukeboxBlock
							{
								JukeboxBlock jbb = Main.JukeboxBlocks.get((evt.getClickedBlock())); // get the JukeboxBlock
								if (jbb != null) // check to make sure the JukeboxBlock is not null (shouldn't happen)
								{
									if (evt.getPlayer().isSneaking()) // check to see if the player is sneaking (holding shift)
									{
										// right clicking a Jukebox while sneaking opens the storage
										Alert.DebugLog("PlayerEvents", "onPlayerInteract.OpenStorage", "Opening Storage GUI");
										jbb.stop(); // stop the song (and remove disc to storage)
										Main.StorageOpen.put(evt.getPlayer().getUniqueId(), jbb); // set the JukeboxBlock to StorageOpen hashmap
										StorageGUI gui = new StorageGUI();
										gui.setDiscs(jbb.getInternalStorage()); // load the internal storage into the GUI
										evt.getPlayer().openInventory(gui.getInventory()); // display the GUI
									} else {
										// right clicking a Jukebox without sneaking opens the controls
										Alert.DebugLog("PlayerEvents", "onPlayerInteract.OpenControls", "Opening Controls GUI");
										Main.ControlsOpen.put(evt.getPlayer().getUniqueId(), jbb); // set the JukeboxBlock to StorageOpen hashmap
										ControlsGUI gui = new ControlsGUI(); 
										ItemStack[] inv = evt.getPlayer().getInventory().getContents(); // Get player inventory
										Main.PlayerInventory.put(evt.getPlayer().getUniqueId(), inv); // Save the inventory to hashmap
										evt.getPlayer().getInventory().clear(); // Clear the inventory
										ItemStack isNA = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
										ItemMeta imNA = isNA.getItemMeta();
										imNA.setDisplayName(".");
										imNA.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
										isNA.setItemMeta(imNA);
										for (int i = 0; i <= 36; i++) { // Loop through the inventory and replace with gray stained glass
											evt.getPlayer().getInventory().setItem(i, isNA);
										}
										
										evt.getPlayer().openInventory(gui.getInventory()); // display the GUI
									}
									evt.setCancelled(true); // cancel the event
								}
							}
						}
					}
				}
			}
		}
	}
}
