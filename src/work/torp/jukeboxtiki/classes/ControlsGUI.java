package work.torp.jukeboxtiki.classes;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import work.torp.jukeboxtiki.Main.IGUI;

public class ControlsGUI implements IGUI{

	
	@Override
    public Inventory getInventory() {
   
        Inventory GUI = Bukkit.createInventory(this, 9, "Jukebox Controls");
   
        ItemStack isNA = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta imNA = isNA.getItemMeta();
		imNA.setDisplayName(".");
		imNA.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isNA.setItemMeta(imNA);
		
        ItemStack isStop = new ItemStack(Material.RED_WOOL, 1);
		ItemMeta imStop = isStop.getItemMeta();
		imStop.setDisplayName("Stop");
		imStop.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isStop.setItemMeta(imStop);
		
        ItemStack isPlay = new ItemStack(Material.GREEN_WOOL, 1);
		ItemMeta imPlay = isPlay.getItemMeta();
		imPlay.setDisplayName("Next Song");
		imPlay.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isPlay.setItemMeta(imPlay);
		
        ItemStack isRemove = new ItemStack(Material.WOODEN_AXE, 1);
		ItemMeta imRemove = isRemove.getItemMeta();
		imRemove.setDisplayName("Remove Jukebox");
		imRemove.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		isRemove.setItemMeta(imRemove);
		
        GUI.setItem(0, isNA);
        GUI.setItem(1, isNA);
        GUI.setItem(2, isNA);
        GUI.setItem(3, isStop);
        GUI.setItem(4, isNA);
        GUI.setItem(5, isPlay);
        GUI.setItem(6, isNA);
        GUI.setItem(7, isNA);
        GUI.setItem(8, isRemove);

        return GUI;
    }

	@Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
		// do nothing - handled in the event
    }
}