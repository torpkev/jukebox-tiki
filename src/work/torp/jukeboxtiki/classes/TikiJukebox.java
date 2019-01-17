package work.torp.jukeboxtiki.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.helpers.Check;
import work.torp.jukeboxtiki.helpers.Convert;


public class TikiJukebox {
	
	// Private objects
	private int jukeboxID;
	private UUID owner;
	private Location location;
	private List<Disc> discs;
	private boolean isPlaying;
	private boolean isActive;
	
	// Getters and Setters
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
	public List<Disc> getDiscs()
	{
		return this.discs;
	}
	public void setDiscs(List<Disc> discs)
	{
		this.discs = discs;
	}
	public boolean getIsPlaying()
	{
		return this.isPlaying;
	}
	public void setIsPlaying(boolean isPlaying)
	{
		this.isPlaying = isPlaying;
	}
	public boolean getIsActive()
	{
		return this.isActive;
	}
	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
	}
	
	// Private Functions
	private Jukebox getJukebox()
	{
		Jukebox retVal = null;
		Block b = this.location.getBlock();
		if (b != null)
		{
			if (b.getType() == Material.JUKEBOX)
			{
				BlockState bs = b.getState();
				if (bs instanceof Jukebox)
				{
					retVal = (Jukebox) bs;
				} else 
				{
					this.isActive = false; // Disable the Jukebox
				}
			}
		}
		return retVal;
	}
	
	// Public Functions
	public void init()
	{
		this.jukeboxID = -1;
		this.owner = UUID.randomUUID();
		this.location = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		this.discs = new ArrayList<Disc>();
		this.isPlaying = false;
		this.isActive = true;
	}
	public void save()
	{
		// TODO: save
	}
	public boolean addDisc(Disc disc)
	{
		boolean retVal = false;
    	try {
	    	if (this.discs == null) {
	    		this.discs = new ArrayList<Disc>();
	    	}
	    	if (disc != null)
	    	{
	    		if (Check.isMusicDisc(disc.getDisc()))
	    		{
	    			Alert.DebugLog("Jukebox", "addDisc", "Adding " + disc.getDisc().name() + " to Jukebox ID: " + Integer.toString(this.jukeboxID));
	    			this.discs.add(disc);
	    			retVal = true;
	    		}
	    	} else {
	    		Error e = new Error();
	    		e.init();
	    		e.set("Jukebox.addDisc.001", "Jukebox", "addDisc", "Add Disc to Jukebox", "No disc passed to function", "Material disc is null", Error.Severity.WARN, UUID.randomUUID(), false, "");
	    		e.recordError();
	    	}
    	} catch (Exception ex) {
    		Error e = new Error();
    		e.init();
    		e.set("Jukebox.addDisc.002", "Jukebox", "addDisc", "Add Disc to Jukebox", "Unexpected Error", ex.getMessage(), Error.Severity.URGENT, UUID.randomUUID(), false, "");
    		e.recordError();
		}
    	return retVal;
	}
	public boolean removeDisc(Disc disc)
	{
		boolean retVal = false;
    	try {
    		if (disc != null)
    		{
    			if (Check.isMusicDisc(disc.getDisc()))
    			{
    				List<Disc> lstJB = new ArrayList<Disc>();
            		if (this.discs != null) {
            			for (Disc m : this.discs)
        	    		{
            				if (!m.equals(disc))
        					{
            					lstJB.add(m);
        					} else {
        						Alert.DebugLog("Jukebox", "removeDisc", "Removing " + disc.getDisc().name() + " from Jukebox ID: " + Integer.toString(this.jukeboxID));
        					}
        	    		}
            		}
        	    	this.discs = lstJB;
        	    	retVal = true;
    			}
    		} else {
	    		Error e = new Error();
	    		e.init();
	    		e.set("Jukebox.removeDisc.001", "Jukebox", "removeDisc", "Removing Disc from Jukebox", "No disc passed to function", "Material disc is null", Error.Severity.WARN, UUID.randomUUID(), false, "");
	    		e.recordError();
    		}
    	} catch (Exception ex) {
    		Error e = new Error();
    		e.init();
    		e.set("Jukebox.removeDisc.002", "Jukebox", "removeDisc", "Removing Disc from Jukebox", "Unexpected Error", ex.getMessage(), Error.Severity.URGENT, UUID.randomUUID(), false, "");
    		e.recordError();
		}
    	return retVal;
	}
	public List<UUID> nearbyPlayers()
	{
		List<UUID> lstUUID = new ArrayList<UUID>();
		if (Bukkit.getOnlinePlayers() != null)
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				if (player != null)
				{
					int dist = (int) Math.round(this.location.distance(player.getLocation()));
					Alert.DebugLog("Jukebox", "nearbyPlayers", "Player UUID: " + player.getUniqueId().toString() + " - Location: " + Convert.LocationToReadableString(player.getLocation()) + " - Distance from Jukebox: " + Integer.toString(dist) + " - Max distance = " + Integer.toString(Main.getInstance().getDistance()));
					if (dist >= Main.getInstance().getDistance())
					{
						Alert.DebugLog("Jukebox", "nearbyPlayers", "Adding Player UUID: " + player.getUniqueId().toString());
						lstUUID.add(player.getUniqueId());
					}
				}
			}
		}
		return lstUUID;
	}
	public boolean isJukeboxPlaying()
	{
		boolean retVal = false;
		Jukebox jb = getJukebox();
		if (jb != null)
		{
			retVal = jb.isPlaying();
		}
		return retVal;
	}
	public boolean isDiscLoaded() {
		boolean retVal = false;
		Jukebox jb = getJukebox();
		if (jb != null)
		{
			if (jb.getRecord() != null)
			{
				if (Check.isMusicDisc(jb.getRecord().getType()))
				{
					retVal = true;
				}
			}
		}
		return retVal;
	}
	public Material getLoadedDiscType() {
		Material retVal = Material.AIR;
		Jukebox jb = getJukebox();
		if (jb != null)
		{
			if (jb.getRecord() != null)
			{
				if (Check.isMusicDisc(jb.getRecord().getType()))
				{
					retVal = jb.getRecord().getType();
				}
			}
		}
		return retVal;
	}
	public boolean ejectDiscToStorage() {
		boolean retVal = false;
		if (isDiscLoaded())
		{
			Jukebox jb = getJukebox();
			Material disc = jb.getRecord().getType();
			Disc d = new Disc();
			d.setDisc(disc);
			d.setOrderBy(this.getDiscs().size() + 1);
			this.addDisc(d); // add the disc to storage
		}
		return retVal;
	}
	public boolean loadFirstDiscToStorage() {
		boolean retVal = false;
		if (isDiscLoaded())
		{
			this.ejectDiscToStorage(); // eject disc
		}
		if (!this.discs.isEmpty() && this.discs.size() > 0) {
	        Disc disc = discs.get(0); // get first disc from storage
			Jukebox jb = getJukebox();
			this.removeDisc(disc); // remove disc from storage
			jb.setPlaying(disc.getDisc());
	    }
		return retVal;
	}
	public void shuffleDiscs() {
		List<Disc> lstD = this.discs;
		if (lstD != null)
		{
			lstD.sort((Disc z1, Disc z2) -> {
			   if (z1.getShuffleUUID().toString().hashCode() > z2.getShuffleUUID().toString().hashCode())
				     return 1;
				   if (z1.getShuffleUUID().toString().hashCode() < z2.getShuffleUUID().toString().hashCode())
				     return -1;
				   return 0;
				});
			this.discs = lstD;
		}
	}
	
}
