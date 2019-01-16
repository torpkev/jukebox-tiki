package work.torp.jukeboxtiki;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;
import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.Jukebox;

public class Main  extends JavaPlugin {
	
	// Hashmaps
	public static HashMap<UUID, UUID> CommandUUID = new HashMap<UUID, UUID>();
	
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

    // Lists
    @SuppressWarnings("unused")
	private List<Jukebox> jukeboxes;
    
    // Configuration
    public void loadConfig() {
    	try {	
//    		String s_console_only = Main.getInstance().getConfig().getString("console_only");
//    		console_only = false; // default to false
//	    	if (s_console_only != null) {
//	    		if (s_console_only.equalsIgnoreCase("true")) {
//	    			console_only = true;
//				} else if (s_console_only.equalsIgnoreCase("true")) {
//					console_only = false;
//				} else {
//					Alert.Log("Main.loadConfig", "console_only value is invalid, using default of false");
//				}
//	    	} else {
//	    		Alert.Log("Main.loadConfig", "console_only value not found, using default of false");
//	    	}	
    	}
    	catch (Exception ex) {
    		Alert.Log("Load Configuration", "Unexpected Error - " + ex.getMessage());  	
    	}
    }
    
    public void loadEventListeners() {
		Alert.VerboseLog("Main", "Starting Event Listeners");	
		try {	
			//Bukkit.getPluginManager().registerEvents(new BlockEvents(), this);
		} catch (Exception ex) {
			Alert.Log("Load Event Listeners", "Unexpected Error - " + ex.getMessage());  
		}
    }
    
    // Commands
    public void loadCommands() {
    	Alert.DebugLog("Main", "loadCommands", "Activating /givespawner");
		try {
		    	//getCommand("givespawner").setExecutor(new GiveSpawner());
		} catch (Exception ex) {
			Alert.Log("Load Commands", "Unexpected Error - " + ex.getMessage());  
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


    	} catch (Exception ex) {
			Alert.DebugLog("Main", "onEnable", "Unexpected Error - " + ex.getMessage());  
		}
	}

    // Disable
    @Override
    public void onDisable() {
    	
	}

}
