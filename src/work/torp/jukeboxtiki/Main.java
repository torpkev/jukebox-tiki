package work.torp.jukeboxtiki;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.JukeboxBlock;
import work.torp.jukeboxtiki.classes.TikiJukebox;
import work.torp.jukeboxtiki.commands.JukeboxCommand;
import work.torp.jukeboxtiki.events.BlockEvents;
import work.torp.jukeboxtiki.events.InventoryEvents;
import work.torp.jukeboxtiki.events.PlayerEvents;
import work.torp.jukeboxtiki.scheduled.PlaySong;

public class Main  extends JavaPlugin {
	
	public interface IGUI extends InventoryHolder{
	    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem);
	}
	
	// Hashmaps
	public static HashMap<UUID, UUID> CommandUUID = new HashMap<UUID, UUID>();
	public static HashMap<Block, TikiJukebox> Jukeboxes = new HashMap<Block, TikiJukebox>();
	public static HashMap<Block, JukeboxBlock> JukeboxBlocks = new HashMap<Block, JukeboxBlock>();
	public static HashMap<TikiJukebox, Boolean> NextSongButton = new HashMap<TikiJukebox, Boolean>();
	public static HashMap<UUID, TikiJukebox> OpenStorage = new HashMap<UUID, TikiJukebox>();
	
	// Main
	private static Main instance;
    public static Main getInstance() {
		return instance;
	}
    
    // DebugFile
	private boolean debugfile;
    public boolean getDebugFile() {
        return this.debugfile;
    }
    public void setDebugFile(boolean debugfile) {
    	this.debugfile = debugfile;
    }

    // Configuration
    private int distance = 64;
    public int getDistance() 
    {
    	return this.distance;
    }
    private boolean internal_storage = true;
    public boolean getInternalStorage()
    {
    	return this.internal_storage;
    }
    public void loadConfig() {
    	try {	
    		if (Main.getInstance().getConfig().getString("distance") != null)
    		{
    			String s_distance = Main.getInstance().getConfig().getString("distance");
    			int i_distance = distance;
    			try{
    				i_distance = Integer.parseInt(s_distance);
    				distance = i_distance;
    			} 
    			catch (NumberFormatException ex) {
    				Alert.DebugLog("Main", "loadConfig", "Config - distance invalid, must be a number. Using default");	
    			}
    		}  
    		
    		String s_internal_storage = Main.getInstance().getConfig().getString("internal_storage");
    		internal_storage = false; // default to false
	    	if (s_internal_storage != null) {
	    		if (s_internal_storage.equalsIgnoreCase("true")) {
	    			internal_storage = true;
				} else if (s_internal_storage.equalsIgnoreCase("true")) {
					internal_storage = false;
				} else {
					Alert.Log("Main.loadConfig", "internal_storage value is invalid, using default of true");
				}
	    	} else {
	    		Alert.Log("Main.loadConfig", "internal_storage value not found, using default of true");
	    	}	
    	}
    	catch (Exception ex) {
    		Alert.Log("Load Configuration", "Unexpected Error - " + ex.getMessage());  	
    	}
    }
    
    public void loadEventListeners() {
		Alert.VerboseLog("Main", "Starting Event Listeners");	
		try {	
			Bukkit.getPluginManager().registerEvents(new BlockEvents(), this);
			Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
			Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
		} catch (Exception ex) {
			Alert.Log("Load Event Listeners", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Commands
    public void loadCommands() {
    	Alert.DebugLog("Main", "loadCommands", "Activating /jukebox");
		try {
			getCommand("jukebox").setExecutor(new JukeboxCommand());
		} catch (Exception ex) {
			Alert.Log("Load Commands", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Scheduled
    public void startPlaySong() {
    	try {
	        BukkitTask task = new BukkitRunnable() {       	
	            public void run() {
	            	PlaySong.Run();
	            }
	        }.runTaskTimer(getInstance(), 40, 40);
	        Alert.DebugLog("Main", "startPlaySong", "startPlaySong running with id " + task.getTaskId());
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "startPlaySong", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Enable
    @Override
	public void onEnable() {
    	
    	try {
			instance = this;

			saveDefaultConfig();
			Alert.Log("Main", "Starting Jukebox-Tiki");

	        loadConfig();
	        loadEventListeners();
	        loadCommands();

	        startPlaySong();
	        
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "onEnable", "Unexpected Error - " + ex.getMessage());  
		}
	}

    // Disable
    @Override
    public void onDisable() {
    	
	}

}
