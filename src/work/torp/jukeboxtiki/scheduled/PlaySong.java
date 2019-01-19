package work.torp.jukeboxtiki.scheduled;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.TikiJukebox;

public class PlaySong {
	public static void Run() {
		HashMap<Block, TikiJukebox> hmJB = Main.Jukeboxes;
		if (hmJB != null)
		{
			for (Entry<Block, TikiJukebox> tjb : hmJB.entrySet()) {
				Block b = tjb.getKey();
				TikiJukebox jb = tjb.getValue();
				if (jb != null)
				{
					if (jb.getIsActive())
					{
						if (b.getState() instanceof Jukebox)
						{
							long milliseconds = new Timestamp(System.currentTimeMillis()).getTime() - jb.getSongEnds().getTime();
							if (jb.getIsPlaying() && milliseconds > 1000)
							{
								if (jb.nearbyPlayers() != null)
								{
									if (!jb.nearbyPlayers().isEmpty())
									{
										Alert.DebugLog("PlaySong", "Run", "Jukebox ID: " + Integer.toString(jb.getJukeboxID()) + " has players nearby - starting next disc");
										Main.NextSongButton.put(jb,  true);
									} else {
										// stop
									}
								}
							}
						}
					}
					
					if (Main.NextSongButton.containsKey(jb))
					{
						Alert.DebugLog("PlaySong", "Run", "Jukebox ID: " + Integer.toString(jb.getJukeboxID()) + " - Next song triggered");
						if (Main.NextSongButton.get(jb)) {
							Main.NextSongButton.remove(jb);
							if (Main.getInstance().getInternalStorage())
							{
								jb.play();
							}
						}
					}
				}
			}
		}
		
		
		
	}
}
