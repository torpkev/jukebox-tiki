package work.torp.jukeboxtiki.scheduled;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.JukeboxBlock;
//import work.torp.jukeboxtiki.classes.TikiJukebox;
import work.torp.jukeboxtiki.helpers.Convert;

public class PlaySong {
	public static void Run() {
		HashMap<Block, JukeboxBlock> hmJB = Main.JukeboxBlocks; // get the JukeboxBlock entries
		if (hmJB != null) // make sure the value isn't null
		{
			for (Entry<Block, JukeboxBlock> jbb : hmJB.entrySet()) { // loop through JukeboxBlock entries
				Block b = jbb.getKey(); // get the Block
				JukeboxBlock jb = jbb.getValue(); // get the JukeboxBlock
				if (jb != null) // check to make sure the JukeboxBlock is not null (shouldn't happen)
				{
					if (jb.getIsActive()) // check to make sure the JukeboxBlock is active
					{
						if (b.getState() instanceof Jukebox) // check to make sure the block is actually a Jukebox object
						{
							if (jb.getIsPlaying() && jb.getSongEnds() != null) // check to make sure the jukebox is playing and the song ends value is not null
							{
								long msec = new Timestamp(System.currentTimeMillis()).getTime() - jb.getSongEnds().getTime(); // check how many milliseconds between current time and song end time
								if (msec > 1000) // if the JukeboxBlock says it is playing, and the song ended at least 1 second ago
								{
									if (jb.nearbyPlayers() != null) // check to make sure JukeboxBlock nearbyPlayers is not returning null (shouldn't happen)
									{
										if (!jb.nearbyPlayers().isEmpty()) // check to make sure there are nearby players in the list
										{
											Alert.DebugLog("PlaySong", "Run", "Jukebox at: " + Convert.LocationToReadableString(jb.getLocation()) + " has players nearby - starting next disc");
											Main.NextSong.put(jb,  true); // record that we want the next song (same as if we clicked Next Song)
										} else {
											//jb.stop(); // no nearby players, so stop
											Alert.DebugLog("PlaySong", "Run", "No nearby players");
										}
									} else {
										Alert.DebugLog("PlaySong", "Run", "No nearby players");
									}
									
								} 
							}
						}
						if (Main.NextSong.containsKey(jb)) // check if next song is requested
						{
							Alert.DebugLog("PlaySong", "NextSong", "Jukebox at: " + Convert.LocationToReadableString(jb.getLocation()) + " - Next song triggered");
							Main.NextSong.remove(jb); // remove next song request
							jb.nextDisc(); // get next disc
							jb.play(); // play the song
						}
					}
				}
			}
		}
	}
}
