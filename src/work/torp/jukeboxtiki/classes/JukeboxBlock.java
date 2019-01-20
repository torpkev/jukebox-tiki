package work.torp.jukeboxtiki.classes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

public class JukeboxBlock {
	private UUID owner;
	private Location location;
	private MusicDisc currentDisc;
	private List<MusicDisc> internalStorage;
	private Timestamp songEnds;
	private boolean isPlaying;
	private boolean isActive;
	
	// Getters
	public UUID getOwner()
	{
		return this.owner;
	}
	public Location getLocation()
	{
		return this.location;
	}
	public MusicDisc getCurrentDisc()
	{
		return this.currentDisc;
	}
	public List<MusicDisc> getInternalStorage()
	{
		return this.internalStorage;
	}
	public Timestamp getSongEnds()
	{
		return this.songEnds;
	}
	public boolean getIsPlaying()
	{
		return this.isPlaying;
	}
	public boolean getIsActive()
	{
		return this.isActive;
	}
	
	// Private Functions
	private Jukebox getJukebox()
	{
		Jukebox retVal = null;
		Block b = this.location.getBlock(); // get the current block
		if (b != null) // check to make sure the block isn't null (shouldn't happen)
		{
			if (b.getType().equals(Material.JUKEBOX)) // double check we're working with a jukebox
			{
				BlockState bs = b.getState(); // Grab the block state so we can cast to Jukebox
				if (bs instanceof Jukebox) // Double check that we can cast (shouldn't not be if material is Jukebox)
				{
					retVal = (Jukebox) bs; // set our return value to be a cast BlockState to Jukebox
				} else 
				{
					this.isActive = false; // Disable the Jukebox
				}
			}
		}
		return retVal; // return the value
	}
	private void setSongEnds()
	{
		if (this.currentDisc != null)
		{
			Calendar cal = Calendar.getInstance(); // Create a new calendar object
			cal.setTimeInMillis(new Timestamp(System.currentTimeMillis()).getTime()); // set the current time
			cal.add(Calendar.SECOND,Provide.getRecordLengthFromDisc(this.currentDisc.getDisc())); // add the number of seconds of the record to the calendar object
			Timestamp ts_new_date_ws = new Timestamp(cal.getTime().getTime()); // get our new time, properly formatted
			this.songEnds = ts_new_date_ws; // set the song ends timestamp 
		} else {
			this.songEnds = null;
		}
	}
	private void sortInternalStorage()
	{
		if (this.internalStorage != null) // check the internal storage isn't null (should never happen)
		{
			List<MusicDisc> lstD = this.internalStorage; // create a copy of the internal storage
			if (lstD != null) // double check our copy isn't null (should never happen as original wasn't null)
			{
				// sort the copy
				lstD.sort((MusicDisc z1, MusicDisc z2) -> {
				   if (z1.getOrderBy() > z2.getOrderBy())
					     return 1;
					   if (z1.getOrderBy() < z2.getOrderBy())
					     return -1;
					   return 0;
					});
				this.internalStorage = lstD; // set the original internal storage from the copy
			}
		}
	}
	private int getNextDiscOrderBy()
	{
		this.sortInternalStorage(); // sort the internal storage so we can properly work out the next to use
		int i = 0;
		if (this.internalStorage != null) // check our internal storage isn't null (should never happen)
		{
			for (MusicDisc md : this.internalStorage) // loop through internal storage
			{
				i = md.getOrderBy(); // grab the order by number
			}
			return i + 1; // return the highest order by number + 1
		} else {
			return 0; // return 0 if internal storage is null (should never happen)
		}
	}
	private void save(boolean hardSave)
	{
		Main.JukeboxBlocks.put(this.getJukebox().getBlock(), this); // update the hashmap with updated values in this JukeboxBlock
		if (hardSave)
		{
			Main.getInstance().getDatabase().saveJukebox(this);
		}
	}
	
	// Public Functions
	public void init(UUID owner, Location location, List<MusicDisc> internalStorage)
	{
		// the init() function should be run when creating the object - it sets our
		// initial values and prevents null errors later on
		this.owner = owner;
		this.location = location;
		this.currentDisc = new MusicDisc();
		this.currentDisc.init();
		this.internalStorage = internalStorage;
		this.setSongEnds();
		this.isPlaying = true;
		this.isActive = true;
		this.save(false); // save to hashmap
		
	}
	public void close()
	{
		this.save(true); // save to hashmap and hard save
		Main.JukeboxBlocks.remove(this.getJukebox().getBlock()); // remove from hashmap
		Main.getInstance().getDatabase().delJukebox(this);
	}
	public List<UUID> nearbyPlayers()
	{
		List<UUID> lstUUID = new ArrayList<UUID>(); // create a new array list to hold the nearby players
		if (Bukkit.getOnlinePlayers() != null) // check to make sure there are players online
		{
			for (Player player : Bukkit.getOnlinePlayers()) // loop through the online players
			{
				if (player != null) // double check the player isn't null (shouldn't happen)
				{
					int dist = (int) Math.round(this.location.distance(player.getLocation())); // get the distance of player from jukebox
					Alert.DebugLog("JukeboxBlock", "nearbyPlayers", "Player UUID: " + player.getUniqueId().toString() + " - Location: " + Convert.LocationToReadableString(player.getLocation()) + " - Distance from Jukebox: " + Integer.toString(dist) + " - Max distance = " + Integer.toString(Main.getInstance().getDistance()));
					if (dist <= Main.getInstance().getDistance()) // if the distance is less than the value in config, add them as a nearby player
					{
						Alert.DebugLog("JukeboxBlock", "nearbyPlayers", "Adding Player UUID: " + player.getUniqueId().toString());
						lstUUID.add(player.getUniqueId()); // add the player to the list
					}
				}
			}
		}
		return lstUUID; // return the list
	}
	public void setInternalStorage(List<MusicDisc> lstMD)
	{
		this.internalStorage = lstMD;
	}
	public void addDisc(MusicDisc disc)
	{
    	try {
	    	if (this.internalStorage == null) { // check that internalStorage isn't null (shouldn't happen after init())
	    		this.internalStorage = new ArrayList<MusicDisc>(); // if it is null for whatever reason, set it to an empty list
	    	}
	    	if (disc != null) // check to make sure we haven't passed a null MusicDisc
	    	{
	    		if (Check.isMusicDisc(disc.getDisc())) // Check to make sure the Material in MusicDisc is a music disc
	    		{
	    			//Alert.DebugLog("Jukebox", "addDisc", "Adding " + disc.getDisc().name() + " to Jukebox ID: " + Integer.toString(this.jukeboxID));
	    			disc.setOrderBy(this.getNextDiscOrderBy()); // set the order by of the disc to be the last order by currently existing + 1
	    			this.internalStorage.add(disc); // add the MusicDisc to internal storage
	    		}
	    	} else {
	    		Error e = new Error(); // create a new error object
	    		e.init(); // initialize the error object
	    		e.set("JukeboxBlock.addDisc.001", "Jukebox", "addDisc", "Add Disc to Jukebox", "No disc passed to function", "MusicDisc disc is null", Error.Severity.WARN, UUID.randomUUID(), false, "");
	    		e.recordError(); // record the error
	    	}
    	} catch (Exception ex) {
    		Error e = new Error(); // create a new error object
    		e.init(); // initialize the error object
    		e.set("JukeboxBlock.addDisc.002", "Jukebox", "addDisc", "Add Disc to Jukebox", "Unexpected Error", ex.getMessage(), Error.Severity.URGENT, UUID.randomUUID(), false, "");
    		e.recordError(); // record the error
		}
    	this.save(false); // save to hashmap only
	}
	public void removeDisc(MusicDisc disc)
	{
    	try { // to remove, we're going to add everything except the one we want to remove to a new list and set that.  That prevents issues with referencing a record we're deleting
    		if (disc != null) // check to make sure we didn't pass a null MusicDisc
    		{
    			if (Check.isMusicDisc(disc.getDisc())) // check to make sure the Material in MusicDisc is a music disc
    			{
    				List<MusicDisc> lstJB = new ArrayList<MusicDisc>(); // create a new list to store the MusicDisc values
            		if (this.internalStorage != null) { // check to make sure that internalStorage isn't null (should never happen after init()) - if null, we don't need to return anything
            			for (MusicDisc m : this.internalStorage) // loop through internalStorage
        	    		{
            				if (!m.equals(disc)) // check if the disc in storage matches the disc we want to remove, if it is not, then add the MusicDisc to the new list
        					{
            					lstJB.add(m); // add to list
        					} else {
        						//Alert.DebugLog("Jukebox", "removeDisc", "Removing " + disc.getDisc().name() + " from Jukebox ID: " + Integer.toString(this.jukeboxID));
        					}
        	    		}
            		}
        	    	this.internalStorage = lstJB; // set internalStorage to our new list
        	    	this.sortInternalStorage(); // sort the list
    			}
    		} else {
	    		Error e = new Error(); // create a new error object
	    		e.init(); // initialize the error object
	    		e.set("JukeboxBlock.removeDisc.001", "Jukebox", "removeDisc", "Removing Disc from Jukebox", "No disc passed to function", "MusicDisc disc is null", Error.Severity.WARN, UUID.randomUUID(), false, "");
	    		e.recordError(); // record the error
    		}
    	} catch (Exception ex) {
    		Error e = new Error(); // create a new error object
    		e.init(); // initialize the error object
    		e.set("JukeboxBlock.removeDisc.002", "Jukebox", "removeDisc", "Removing Disc from Jukebox", "Unexpected Error", ex.getMessage(), Error.Severity.URGENT, UUID.randomUUID(), false, "");
    		e.recordError(); // record the error
		}
    	this.save(false); // save to hashmap only
	}
	public void nextDisc()
	{
		Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "nextDisc() started");
		if (currentDisc != null) // check if the current disc isnt set
		{
			Alert.DebugLog("JukeboxBlock", "nextDisc", "Stopping song");
			this.stop(); // stop song if we have one in there, this will also eject the disc to storage			
		}
		if (this.internalStorage != null) // check our internal storage isn't null (should never happen)
		{
			Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "internalStorage is not null");
			
			if (!this.internalStorage.isEmpty())
			{
				Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "internalStorage is not empty");
				// get the first record
				MusicDisc md = this.internalStorage.get(0); // get first disc
				Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "MusicDisc md = " + md.getDisc().name());
				this.currentDisc = md; // set the current disc to be the first disc
				Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "currentDisc = " + currentDisc.getDisc().name());
				removeDisc(md); // remove first disc from storage
				Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "MusicDisc " + md.getDisc().name() + " removed from storage");
				this.sortInternalStorage(); // sort the storage
				Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "internalStorage sorted");
			}
			if (this.internalStorage != null) // check our internal storage isn't null (should never happen)
			{
				if (!this.internalStorage.isEmpty())
				{
					for (MusicDisc md : this.internalStorage)
					{
						Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "storage after getting next disc: " + md.getDisc().name());
					}
				}
			}
		} else {
			Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "internal storage is null");
		}
		Alert.DebugLog(ChatColor.RED + ">>>", ChatColor.RED +">>>", "saving");
		this.save(false); // save to hashmap only
	}
	public void stop()
	{
		Alert.DebugLog(">>>", ">>>", ChatColor.GOLD + "stop() started");
		Alert.DebugLog("JukeboxBlock", "stop", "Stopping & ejecting");
		if (this.internalStorage != null) // check our internal storage isn't null (should never happen)
		{
			if (this.currentDisc != null) // check to make sure current disc is not null
			{
				Alert.DebugLog("JukeboxBlock", "stop", "Adding current disc to storage: " + this.currentDisc.getDisc().name());
				addDisc(this.currentDisc); // move disc to internal storage
			}
			else 
			{
				Alert.DebugLog("JukeboxBlock", "stop", "current disc is null - nothing to do here");
			}
			Alert.DebugLog("JukeboxBlock", "stop", "clearing current disc & setting isplaying = false");
			this.currentDisc = null; // Clear current MusicDisc
			this.isPlaying = false; // Set playing flag to false
		}
		Alert.DebugLog("JukeboxBlock", "stop", "Ejecting disc");
		this.getJukebox().eject(); // eject the disc
		this.getLocation().getBlock().setType(Material.JUKEBOX); // replace the block (the ItemSpawnEvent is cancelled so record never gets cleared)
		this.save(false); // save to hashmap only
		Alert.DebugLog(">>>", ">>>", ChatColor.GOLD + "stop() ended");
	}
	public void play()
	{
		Alert.DebugLog(ChatColor.YELLOW + ">>>", ChatColor.YELLOW +">>>", ChatColor.AQUA + "play() started");
		Jukebox jb = this.getJukebox();
		if (!this.isPlaying)
		{
			if (this.currentDisc == null) // check to see if we have a disc loaded
			{
				Alert.Log("JukeboxBlock.play", "Getting the next disc");
				this.nextDisc(); // as there is no current disc, grab the next one if we can
			}
			if (this.currentDisc != null) // check to see if we have a disc now, if we do, play
			{
				this.setSongEnds(); // set the timestamp the song ends
				ItemStack istack = new ItemStack(this.currentDisc.getDisc(), 1); // create item stack
				jb.setRecord(istack); // set the record into the block
				jb.setPlaying(this.getCurrentDisc().getDisc()); // set the record to play
				jb.update();
				this.save(false); // Save to hashmap only
			} else {
				Alert.Log("JukeboxBlock.play", "Current disc is null");
				this.isPlaying = false; // update our playing flag to show false
			}
		} else {
			Alert.Log("JukeboxBlock.play", "isPlaying is set to false");
		}
	}
}

