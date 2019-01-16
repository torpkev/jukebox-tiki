package work.torp.jukeboxtiki.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.helpers.Check;


public class Jukebox {
	
	private int jukeboxID;
	private UUID owner;
	private Location location;
	private List<Material> discs;

	public int getJukeboxID()
	{
		return this.jukeboxID;
	}
	public void setJukeboxID(int jukeboxID)
	{
		this.jukeboxID = jukeboxID;
	}
	public UUID getOwner()
	{
		return this.owner;
	}
	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}
	public Location getLocation()
	{
		return this.location;
	}
	public void setLocation(Location location)
	{
		this.location = location;
	}
	public List<Material> getDiscs()
	{
		return this.discs;
	}
	public void setDiscs(List<Material> discs)
	{
		this.discs = discs;
	}
	public void addDisc(Material disc)
	{
    	try {
	    	if (this.discs == null) {
	    		this.discs = new ArrayList<Material>();
	    	}
	    	if (disc != null)
	    	{
	    		if (Check.isMusicDisc(disc))
	    		{
	    			Alert.DebugLog("Jukebox", "addDisc", "Adding " + disc.name() + " to Jukebox ID: " + Integer.toString(this.jukeboxID));
	    			this.discs.add(disc);
	    		}
	    	} else {
	    		Alert.DebugLog("Jukebox", "addDisc", "No disc passed to function");
	    	}
    	} catch (Exception ex) {
			Alert.DebugLog("Jukebox", "addDisc", "Unexpected Error - " + ex.getMessage());  
		}
	}
	public void removeDisc(Material disc)
	{
    	try {
    		if (disc != null)
    		{
    			if (Check.isMusicDisc(disc))
    			{
    				List<Material> lstJB = new ArrayList<Material>();
            		if (this.discs != null) {
            			for (Material m : this.discs)
        	    		{
            				if (!m.equals(disc))
        					{
            					lstJB.add(m);
        					} else {
        						Alert.DebugLog("Jukebox", "removeDisc", "Removing " + disc.name() + " from Jukebox ID: " + Integer.toString(this.jukeboxID));
        					}
        	    		}
            		}
        	    	discs = lstJB;
    			}
    		} else {
    			Alert.DebugLog("Jukebox", "removeDisc", "No disc passed to function");
    		}
    	} catch (Exception ex) {
			Alert.DebugLog("Jukebox", "removeDisc", "Unexpected Error - " + ex.getMessage());  
		}
	}
}
