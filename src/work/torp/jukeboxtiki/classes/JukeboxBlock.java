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
	
	// Getters (not exposing my Setters as they shouldn't be mutable)
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
			// TODO: save to file/database
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
		this.isPlaying = true; // TODO: Set to true so it starts immediately
		this.isActive = true;
		this.save(true); // save to hashmap and hard save
	}
	public void close()
	{
		this.save(true); // save to hashmap and hard save
		Main.JukeboxBlocks.remove(this.getJukebox().getBlock()); // remove from hashmap
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
					Alert.DebugLog("JukeboxBlock", "nearbyPlayers", "Player UUID: " + player.getUniqueId().toString() + " - Location: " + Convert.LocationToReadableString(player.getLocation()) + " - Distance from Jukebox: " + Integer.toString(dist) + " - Max distance = " + Integer.toString(Main.getInstance().getDistance()));
					if (dist <= Main.getInstance().getDistance())
					{
						Alert.DebugLog("JukeboxBlock", "nearbyPlayers", "Adding Player UUID: " + player.getUniqueId().toString());
						lstUUID.add(player.getUniqueId());
					}
				}
			}
		}
		return lstUUID;
	}
	public void addDisc(MusicDisc disc)
	{
    	try {
	    	if (this.internalStorage == null) {
	    		this.internalStorage = new ArrayList<MusicDisc>();
	    	}
	    	if (disc != null)
	    	{
	    		if (Check.isMusicDisc(disc.getDisc()))
	    		{
	    			//Alert.DebugLog("Jukebox", "addDisc", "Adding " + disc.getDisc().name() + " to Jukebox ID: " + Integer.toString(this.jukeboxID));
	    			disc.setOrderBy(this.getNextDiscOrderBy());
	    			this.internalStorage.add(disc);
	    		}
	    	} else {
	    		Error e = new Error();
	    		e.init();
	    		e.set("JukeboxBlock.addDisc.001", "Jukebox", "addDisc", "Add Disc to Jukebox", "No disc passed to function", "MusicDisc disc is null", Error.Severity.WARN, UUID.randomUUID(), false, "");
	    		e.recordError();
	    	}
    	} catch (Exception ex) {
    		Error e = new Error();
    		e.init();
    		e.set("JukeboxBlock.addDisc.002", "Jukebox", "addDisc", "Add Disc to Jukebox", "Unexpected Error", ex.getMessage(), Error.Severity.URGENT, UUID.randomUUID(), false, "");
    		e.recordError();
		}
    	this.save(false); // save to hashmap only
	}
	public void removeDisc(MusicDisc disc)
	{
    	try {
    		if (disc != null)
    		{
    			if (Check.isMusicDisc(disc.getDisc()))
    			{
    				List<MusicDisc> lstJB = new ArrayList<MusicDisc>();
            		if (this.internalStorage != null) {
            			for (MusicDisc m : this.internalStorage)
        	    		{
            				if (!m.equals(disc))
        					{
            					lstJB.add(m);
        					} else {
        						//Alert.DebugLog("Jukebox", "removeDisc", "Removing " + disc.getDisc().name() + " from Jukebox ID: " + Integer.toString(this.jukeboxID));
        					}
        	    		}
            		}
        	    	this.internalStorage = lstJB;
        	    	this.sortInternalStorage();
    			}
    		} else {
	    		Error e = new Error();
	    		e.init();
	    		e.set("JukeboxBlock.removeDisc.001", "Jukebox", "removeDisc", "Removing Disc from Jukebox", "No disc passed to function", "MusicDisc disc is null", Error.Severity.WARN, UUID.randomUUID(), false, "");
	    		e.recordError();
    		}
    	} catch (Exception ex) {
    		Error e = new Error();
    		e.init();
    		e.set("JukeboxBlock.removeDisc.002", "Jukebox", "removeDisc", "Removing Disc from Jukebox", "Unexpected Error", ex.getMessage(), Error.Severity.URGENT, UUID.randomUUID(), false, "");
    		e.recordError();
		}
    	this.save(false); // save to hashmap only
	}
	public void nextDisc()
	{
		if (currentDisc != null) // check if the current disc isnt set
		{
			this.stop(); // stop song if we have one in there, this will also eject the disc to storage
		}
		if (this.internalStorage != null) // check our internal storage isn't null (should never happen)
		{
			addDisc(this.internalStorage.get(0)); // get first disc in storage
			this.currentDisc = this.internalStorage.get(0); // set the current disc value so we know it's loaded
		}
		this.save(false); // save to hashmap only
	}
	public void stop()
	{
		if (this.isPlaying)
		{
			if (this.internalStorage != null) // check our internal storage isn't null (should never happen)
			{
				this.currentDisc = null; // Clear current MusicDisc
				this.isPlaying = false; // Set playing flag to false
				removeDisc(this.currentDisc); // move disc to internal storage
			}
			this.getJukebox().eject(); // eject the disc
			this.getLocation().getBlock().setType(Material.JUKEBOX); // replace the block (the ItemSpawnEvent is cancelled so record never gets cleared)
			this.save(false); // save to hashmap only
		}
	}
	public void play()
	{
		if (!this.isPlaying)
		{
			if (this.getCurrentDisc() == null)
			{
				this.nextDisc(); // as there is no current disc, grab the next one
			}
			this.setSongEnds(); // set the timestamp the song ends
			this.getJukebox().setRecord(new ItemStack(this.getCurrentDisc().getDisc(), 1)); // set the record into the block
			this.getJukebox().update(); // update the block (possibly overkill)
			this.getJukebox().setPlaying(this.getCurrentDisc().getDisc()); // set the record to play
			this.getJukebox().update(); // update the block again (almost certainly overkill)
			this.save(false); // Save to hashmap only
		}
	}
}

