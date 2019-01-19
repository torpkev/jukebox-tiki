package work.torp.jukeboxtiki.classes;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import work.torp.jukeboxtiki.Main.IGUI;

public class ExampleGUI implements IGUI{
	 
	private Set<Disc> lstDisc;
	public void setDiscs(Set<Disc> lstDisc)
	{
		this.lstDisc = lstDisc;
	}
	
	@Override
    public Inventory getInventory() {
   
        Inventory GUI = Bukkit.createInventory(this, 54, "Jukebox Storage");
   
        if (lstDisc != null)
        {
        	for (Disc d : lstDisc)
        	{
        		GUI.addItem(new ItemStack(d.getDisc(), 1));
        	}
        }

        return GUI;
    }

	@Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
		
    }
	 
	/*
	private int _currentPage = 0;
    @Override
    public Inventory getInventory() {
   
        Inventory GUI = Bukkit.createInventory(this, 54, "Current page: " + _currentPage + 1);
   
        if(_currentPage == 0)
            GUI.setItem(4, new ItemStack(Material.FEATHER));
        else if(_currentPage == 1)
            GUI.setItem(4, new ItemStack(Material.BARRIER));
   
        return GUI;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        if(clickedItem == null || clickedItem.getType().equals(Material.AIR))
            return;
   
        if(_currentPage == 0 && slot == 1) {
            whoClicked.openInventory(this.setPage(this.getPage() + 1).getInventory());
        }
        else if(_currentPage == 1 && slot == 1) {
            whoClicked.closeInventory();
        }
    }

    public ExampleGUI setPage(int page) {
        if(page < 0)
            _currentPage = 0;
        else if(page > 1)
            _currentPage = 1;
        else
            _currentPage = page;
        return this;
    }
 
    public int getPage() {
        return _currentPage;
    }
    */
}