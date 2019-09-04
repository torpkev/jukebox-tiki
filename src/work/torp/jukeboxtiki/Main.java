package work.torp.jukeboxtiki;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import work.torp.jukeboxtiki.database.Database;
import work.torp.jukeboxtiki.database.SQLite;
import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.JukeboxBlock;
import work.torp.jukeboxtiki.commands.JukeboxCommand;
import work.torp.jukeboxtiki.events.BlockEvents;
import work.torp.jukeboxtiki.events.EntityEvents;
import work.torp.jukeboxtiki.events.InventoryEvents;
import work.torp.jukeboxtiki.events.PlayerEvents;
import work.torp.jukeboxtiki.scheduled.PlaySong;

public class Main  extends JavaPlugin {
	
	public interface IGUI extends InventoryHolder{
	    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem);
	}
	
    // Database
	private Database db;
    public Database getDatabase() {
        return this.db;
    }
    
	// Hashmaps
    // TODO:  Make these private and provide getters & setters
	public static HashMap<UUID, UUID> CommandUUID = new HashMap<UUID, UUID>();
	public static HashMap<Block, JukeboxBlock> JukeboxBlocks = new HashMap<Block, JukeboxBlock>();
	public static HashMap<JukeboxBlock, Boolean> NextSong = new HashMap<JukeboxBlock, Boolean>();
	public static HashMap<UUID, JukeboxBlock> StorageOpen = new HashMap<UUID, JukeboxBlock>();
	public static HashMap<UUID, JukeboxBlock> ControlsOpen = new HashMap<UUID, JukeboxBlock>();
	public static HashMap<UUID, ItemStack[]> PlayerInventory = new HashMap<UUID, ItemStack[]>();
	
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
    // TODO:  config to its own class
    private int distance = 64;
    public int getDistance() 
    {
    	return this.distance;
    }
    private int auto_save_secs = 600;

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
	    	
    		if (Main.getInstance().getConfig().getString("auto_save_secs") != null)
    		{
    			String s_auto_save_secs = Main.getInstance().getConfig().getString("auto_save_secs");
    			int i_auto_save_secs = auto_save_secs;
    			try{
    				i_auto_save_secs = Integer.parseInt(s_auto_save_secs);
    				auto_save_secs = i_auto_save_secs;
    			} 
    			catch (NumberFormatException ex) {
    				Alert.DebugLog("Main", "loadConfig", "Config - distance auto_save_secs, must be a number. Using default");	
    			}
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
			Bukkit.getPluginManager().registerEvents(new EntityEvents(), this);
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
    public void startAutoSave() {
    	try {
	        BukkitTask task = new BukkitRunnable() {       	
	            public void run() {
	            	PlaySong.Run();
	            }
	        }.runTaskTimer(getInstance(), auto_save_secs, auto_save_secs);
	        Alert.DebugLog("Main", "startAutoSave", "startAutoSave running with id " + task.getTaskId());
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "startAutoSave", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Enable
    @Override
	public void onEnable() {
    	
    	try {
			instance = this;

			saveDefaultConfig();
			Alert.Log("Main", "Starting Jukebox-Tiki");

			this.db = new SQLite(this); // New SQLite
	        this.db.load(); // Run load
	        this.db.initialize(); // Run initialize
	        
	        loadConfig();
	        loadEventListeners();
	        loadCommands();

	        getDatabase().getJukebox(); // get entries from database
	        
	        startPlaySong(); // Music loop
	        startAutoSave(); // Auto save feature - default, every 10 mins
	        
    	} catch (Exception ex) {
			Alert.DebugLog("Main", "onEnable", "Unexpected Error - " + ex.getMessage());  
		}
	}

    // Disable
    @Override
    public void onDisable() {
    	HashMap<Block, JukeboxBlock> hmJB = Main.JukeboxBlocks; // get the JukeboxBlock entries
		if (hmJB != null) // make sure the value isn't null
		{
			for (Entry<Block, JukeboxBlock> jbb : hmJB.entrySet()) { // loop through JukeboxBlock entries
				jbb.getValue().stop();
				getDatabase().saveJukebox(jbb.getValue());
			}
		}
	}

}
