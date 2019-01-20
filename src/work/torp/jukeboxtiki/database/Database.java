package work.torp.jukeboxtiki.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;

import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.JukeboxBlock;
import work.torp.jukeboxtiki.classes.MusicDisc;
import work.torp.jukeboxtiki.helpers.Check;
import work.torp.jukeboxtiki.helpers.Convert;
import work.torp.jukeboxtiki.Main;

public abstract class Database {
    Main plugin;
    Connection connection;

    public String SQLConnectionExecute = "Couldn't execute SQL statement: ";
    public String SQLConnectionClose = "Failed to close SQL connection: "; 
    public String NoSQLConnection = "Unable to retreive SQL connection: ";
    public String NoTableFound = "Database Error: No Table Found";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    
    public Database(Main instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM jukebox");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);
   
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, NoSQLConnection, ex);
        }
    }

    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
        	DatabaseError.close(plugin, ex);
        }
    }   

    public void getJukebox() {
    	
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * from jukebox;");  
            rs = ps.executeQuery();

            while(rs.next()){
            	try
            	{
            		Location loc = Convert.LocationFromXYZ(rs.getString("world"), Integer.parseInt(rs.getString("x")), Integer.parseInt(rs.getString("y")), Integer.parseInt(rs.getString("z")));
            		if (loc != null)
            		{
            			JukeboxBlock jbb = new JukeboxBlock(); // create a new JukeboxBlock
        				jbb.init(UUID.fromString(rs.getString("uuid")), loc, new ArrayList<MusicDisc>()); // initialize the JukeboxBlock
        				String [] items = rs.getString("storage").split(",");
        				List<String> container = Arrays.asList(items);
        				List<MusicDisc> lstMD = new ArrayList<MusicDisc>();
        				int iOrdBy = 0;
        				for (String c : container)
        				{
        					Material m = Convert.StringToMaterial(c);
        					if (Check.isMusicDisc(m))
        					{
        						MusicDisc md = new MusicDisc();
        						md.init();
        						md.set(m, iOrdBy);
        						lstMD.add(md);
        					}
        					iOrdBy++;
        				}
        				if (!lstMD.isEmpty())
        				{
        					jbb.setInternalStorage(lstMD);
        				}
        				Main.JukeboxBlocks.put(jbb.getLocation().getBlock(), jbb);
            		}
            	}
            	catch (Exception ex)
            	{
            		Alert.VerboseLog("Database.getJukebox", "Unexpected error getting jukeboxes from database: " + ex.getMessage());
            	}
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, SQLConnectionExecute, ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void saveJukebox(JukeboxBlock jbb) {

    	Connection conn = null;
        PreparedStatement psDel = null;
        PreparedStatement psDb = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	StringBuilder nameBuilder = new StringBuilder();
        	if (jbb.getInternalStorage() != null)
        	{
        		if (!jbb.getInternalStorage().isEmpty())
        		{
        			for (MusicDisc md : jbb.getInternalStorage())
        			{
        				nameBuilder.append(md.getDisc().name()).append(",");
        			}
        			if (nameBuilder.length() > 0)
        			{
        				nameBuilder.deleteCharAt(nameBuilder.length() - 1);
        			}
        		}
        	}
            
            
            sql = "DELETE FROM jukebox WHERE world = '" + jbb.getLocation().getWorld().getName() + "' AND x = " + Integer.toString(jbb.getLocation().getBlockX()) + " AND y = " + Integer.toString(jbb.getLocation().getBlockY()) + " AND z = " + Integer.toString(jbb.getLocation().getBlockZ()) + "; ";
            psDel = conn.prepareStatement(sql);
            psDel.executeUpdate();
            
            sql = "INSERT INTO jukebox (world, x, y, z, storage, uuid) VALUES (" +
        			"'" + jbb.getLocation().getWorld().getName() + "', " +
        			"" + Integer.toString(jbb.getLocation().getBlockX()) + ", " +
        			"" + Integer.toString(jbb.getLocation().getBlockY()) + ", " +
        			"" + Integer.toString(jbb.getLocation().getBlockZ()) + ", " +
        			"'" + nameBuilder.toString() + "', " +
        			"'" + jbb.getOwner().toString() + "' " +
        			");";

            psDb = conn.prepareStatement(sql);
            psDb.executeUpdate();

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.saveJukebox", "Unexpected error saving jukebox");
    		Alert.DebugLog("Database", "saveJukebox", "Unexpected error saving jukebox: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDel != null) {
                    psDel.close();
                }
                if (psDb != null) {
                    psDb.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    }
    public void delJukebox(JukeboxBlock jbb) {

    	Connection conn = null;
        PreparedStatement psDel = null;
        String sql = "";
        try {
        	conn = getSQLConnection();

        	StringBuilder nameBuilder = new StringBuilder();
        	if (jbb.getInternalStorage() != null)
        	{
        		if (!jbb.getInternalStorage().isEmpty())
        		{
        			for (MusicDisc md : jbb.getInternalStorage())
        			{
        				nameBuilder.append(md.getDisc().name()).append(",");
        			}
        			if (nameBuilder.length() > 0)
        			{
        				nameBuilder.deleteCharAt(nameBuilder.length() - 1);
        			}
        		}
        	}
            
            
            sql = "DELETE FROM jukebox WHERE world = '" + jbb.getLocation().getWorld().getName() + "' AND x = " + Integer.toString(jbb.getLocation().getBlockX()) + " AND y = " + Integer.toString(jbb.getLocation().getBlockY()) + " AND z = " + Integer.toString(jbb.getLocation().getBlockZ()) + "; ";
            psDel = conn.prepareStatement(sql);
            psDel.executeUpdate();

    	} catch (Exception ex) {
    		Alert.VerboseLog("Database.delJukebox", "Unexpected error saving jukebox");
    		Alert.DebugLog("Database", "delJukebox", "Unexpected error saving jukebox: " + ex.getMessage() + " - SQL: " + sql);
        } finally {
            try {
                if (psDel != null)
                    psDel.close();            	
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, SQLConnectionClose, ex);
            }
        }
    } 

}