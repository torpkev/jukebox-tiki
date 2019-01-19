package work.torp.jukeboxtiki.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.ExampleGUI;
import work.torp.jukeboxtiki.classes.TikiJukebox;
import work.torp.jukeboxtiki.helpers.Check;

public class PlayerEvents implements Listener {
	@EventHandler
	public void b(ItemSpawnEvent e)
	{
		if (e.getEntity().getItemStack() != null)
		{
			if (Check.isMusicDisc(e.getEntity().getItemStack().getType()))
			{
				Alert.Log("MUSIC DISC", "");
				e.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt)
	{
	
		if (evt.getClickedBlock() != null)
		{
			if (evt.getClickedBlock().getType().equals(Material.JUKEBOX))
			{
				if (evt.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					if (evt.getHand() != null)
					{
						if (evt.getHand() == EquipmentSlot.HAND)
						{	
							if (Main.getInstance().getInternalStorage())
							{
								if (Main.Jukeboxes.containsKey(evt.getClickedBlock()))
								{
									TikiJukebox jb = Main.Jukeboxes.get(evt.getClickedBlock());
									if (jb != null)
									{
										
										if (evt.getPlayer().isSneaking())
										{
											jb.setIsPlaying(false);
											jb.ejectDiscToStorage();
											Alert.Player("Open storage", evt.getPlayer(), true);
											Main.OpenStorage.put(evt.getPlayer().getUniqueId(), jb);
											ExampleGUI gui = new ExampleGUI();
											gui.setDiscs(jb.getDiscs());
											evt.getPlayer().openInventory(gui.getInventory());
										} else {
											// TODO: Controls GUI
											Alert.Player("Open controls", evt.getPlayer(), true);
											jb.stop();
										}
										evt.setCancelled(true);
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
