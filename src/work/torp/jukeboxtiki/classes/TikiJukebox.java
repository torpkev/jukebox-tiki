package work.torp.jukeboxtiki.classes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.helpers.Check;
import work.torp.jukeboxtiki.helpers.Convert;
import work.torp.jukeboxtiki.helpers.Provide;


public class TikiJukebox {
	
	// Private objects
	private int jukeboxID;
	private UUID owner;
	private Location location;
	private Set<Disc> discs;
	private boolean isPlaying;
	private boolean isActive;
	private Timestamp songEnds;
	
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
	public Set<Disc> getDiscs()
	{
		return this.discs;
	}
	public void setDiscs(Set<Disc> discs)
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
	public Timestamp getSongEnds()
	{
		return this.songEnds;
	}
	public void setSongEnds(Material disc)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Timestamp(System.currentTimeMillis()).getTime());
		cal.add(Calendar.SECOND,Provide.getRecordLengthFromDisc(disc));
		Timestamp ts_new_date_ws = new Timestamp(cal.getTime().getTime());
		this.songEnds = ts_new_date_ws; 		
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
		this.discs = new HashSet<Disc>();
		this.isPlaying = true;
		this.isActive = true;
		this.setSongEnds(Material.AIR);
		if (Main.getInstance().getInternalStorage())
		{
			List<String> lstStocked = Main.getInstance().getConfig().getStringList("internal_prestocked");
			if (lstStocked != null)
			{
				for (String discname : lstStocked)
				{
					Disc d = Provide.getDiscByName(discname);
					if (d.getDisc() != Material.AIR)
					{
						this.addDisc(d);
					}
				}
			}
		}
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
	    		this.discs = new HashSet<Disc>();
	    	}
	    	if (disc != null)
	    	{
	    		if (Check.isMusicDisc(disc.getDisc()))
	    		{
	    			//Alert.DebugLog("Jukebox", "addDisc", "Adding " + disc.getDisc().name() + " to Jukebox ID: " + Integer.toString(this.jukeboxID));
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
    				Set<Disc> lstJB = new HashSet<Disc>();
            		if (this.discs != null) {
            			for (Disc m : this.discs)
        	    		{
            				if (!m.equals(disc))
        					{
            					lstJB.add(m);
        					} else {
        						//Alert.DebugLog("Jukebox", "removeDisc", "Removing " + disc.getDisc().name() + " from Jukebox ID: " + Integer.toString(this.jukeboxID));
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
					if (dist <= Main.getInstance().getDistance())
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
	public Block getBlock()
	{
		Jukebox jb = getJukebox();
		return jb.getBlock();
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
		Alert.Log("TikiJukebox", ChatColor.YELLOW + "EJECTING!");
		boolean retVal = false;
		if (isDiscLoaded())
		{
			stop();
			Alert.Log("TikiJukebox", ChatColor.YELLOW + "Disc is loaded");
			Jukebox jb = getJukebox();
			Alert.Log("TikiJukebox", ChatColor.YELLOW + "Got jukebox");
			Material disc = jb.getRecord().getType();
			Disc d = new Disc();
			d.init();
			d.setDisc(disc);
			Alert.Log("TikiJukebox", ChatColor.YELLOW + "Setting disc orderby as " + Integer.toString(this.getDiscs().size() + 1));
			d.setOrderBy(this.getDiscs().size() + 1);
			Alert.Log("TikiJukebox", ChatColor.YELLOW + "Adding disc to storage");
			this.addDisc(d); // add the disc to storage

			jb.getLocation().getBlock().setType(Material.JUKEBOX);
			
			jb.setPlaying(null);
			Main.Jukeboxes.put(jb.getBlock(), this);
			
			
		}
		return retVal;
	}
	public void stop()
	{
		Jukebox jb = getJukebox();
		Material disc = jb.getRecord().getType();
		Disc d = new Disc();
		d.init();
		d.setDisc(disc);
		d.setOrderBy(this.getDiscs().size() + 1);
		this.addDisc(d); // add the disc to storage	
		jb.eject();
		this.isPlaying = false;
	}
	public void play()
	{
		Alert.DebugLog("TikiJukebox", "play", "play() called");
		Jukebox jb = getJukebox();
		boolean playing = false;
		if (this.discs != null)
		{
			if (!this.discs.isEmpty())
			{
				for (Disc d : this.discs)
				{
					Alert.DebugLog("TikiJukebox", "play", ChatColor.GOLD + "Disc in storage: " + d.getDisc().name());
				}
				if (isDiscLoaded())
				{
					Alert.DebugLog("TikiJukebox", "play", "Disc is loaded: " + jb.getRecord().getType());
					this.ejectDiscToStorage(); // eject disc	
				}
				if (!this.discs.isEmpty() && this.discs.size() > 0) {
					for (Disc d : discs)
					{
						if (!playing)
						{
							if (Check.isMusicDisc(d.getDisc()))
							{
								if (jb != null) // TODO: Previous song plays briefly at the start of the next song
								{
									Alert.DebugLog("TikiJukebox", "play", "Playing disc: " + d.getDisc().name());
									this.setSongEnds(d.getDisc());
									this.removeDisc(d); // remove disc from storage
									jb.setRecord(new ItemStack(d.getDisc(), 1));
									jb.update();
									jb.setPlaying(jb.getRecord().getType());
									playing = true;
									Main.Jukeboxes.put(jb.getBlock(), this);
									break;
								} else {
									Alert.DebugLog("TikiJukebox", "play", "Jukebox not found");
								}
							} else {
								Alert.DebugLog("TikiJukebox", "play", "Disc is not reporting as a music disc");
							}
						}
					}
			    }
				if (!playing)
				{
					Alert.DebugLog("TikiJukebox", "play", "Not playing");
				}
			} else {
				Alert.DebugLog("TikiJukebox", "play", "No discs found");
			}
		} else {
			Alert.DebugLog("TikiJukebox", "play", "No discs found");
		}
	}
	

//	public void shuffleDiscs() {
//		Set<Disc> lstD = this.discs;
//		if (lstD != null)
//		{
//			lstD.sort((Disc z1, Disc z2) -> {
//			   if (z1.getShuffleUUID().toString().hashCode() > z2.getShuffleUUID().toString().hashCode())
//				     return 1;
//				   if (z1.getShuffleUUID().toString().hashCode() < z2.getShuffleUUID().toString().hashCode())
//				     return -1;
//				   return 0;
//				});
//			this.discs = lstD;
//		}
//	}
	
}
