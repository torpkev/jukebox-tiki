package work.torp.jukeboxtiki.classes;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import work.torp.jukeboxtiki.Main.IGUI;

public class StorageGUI implements IGUI{

	private List<MusicDisc> lstDisc;
	public void setDiscs(List<MusicDisc> lstDisc)
	{
		this.lstDisc = lstDisc;
	}
	
	@Override
    public Inventory getInventory() {
   
        Inventory GUI = Bukkit.createInventory(this, 27, "Jukebox Storage");
   
        if (lstDisc != null)
        {
        	for (MusicDisc d : lstDisc)
        	{
        		GUI.addItem(new ItemStack(d.getDisc(), 1));
        	}
        }

        return GUI;
    }

	@Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
		// do nothing
    }

}