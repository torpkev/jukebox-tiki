package work.torp.jukeboxtiki.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import work.torp.jukeboxtiki.Main;

public class PlayerEvents implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt)
	{
		if (evt.getClickedBlock() != null)
		{
			if (evt.getClickedBlock().getType().equals(Material.JUKEBOX))
			{
				if (evt.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					if (evt.getPlayer().isSneaking())
					{
						if (Main.getInstance().getInternalStorage())
						{
							// TODO: Storage GUI
						}
					} else {
						// TODO: Controls GUI
					}
				}
			}
		}
	}
}
