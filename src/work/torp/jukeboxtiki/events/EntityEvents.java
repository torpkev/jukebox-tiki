package work.torp.jukeboxtiki.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import work.torp.jukeboxtiki.helpers.Check;

public class EntityEvents implements Listener {
	@EventHandler
	public void b(ItemSpawnEvent e)
	{
		// ItemSpawnEvent is used when ejecting a disc from the Jukebox
		// rather than the BlockDispenseEvent - as we're using internal storage
		// we need to cancel this spawn event
		if (e.getEntity().getItemStack() != null) // check to make sure the item spawned is an ItemStack (not null)
		{
			if (Check.isMusicDisc(e.getEntity().getItemStack().getType())) // check to make sure the spawned ItemStack is a music disc
			{
				e.setCancelled(true); // if it is a music disc, cancel the event
			}
		}
	}
}
