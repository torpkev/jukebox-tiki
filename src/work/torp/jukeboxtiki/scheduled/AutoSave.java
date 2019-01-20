package work.torp.jukeboxtiki.scheduled;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.block.Block;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.classes.JukeboxBlock;

public class AutoSave {
	public static void Run() {
    	HashMap<Block, JukeboxBlock> hmJB = Main.JukeboxBlocks; // get the JukeboxBlock entries
		if (hmJB != null) // make sure the value isn't null
		{
			for (Entry<Block, JukeboxBlock> jbb : hmJB.entrySet()) { // loop through JukeboxBlock entries
				Main.getInstance().getDatabase().saveJukebox(jbb.getValue());
			}
		}
	}
}
