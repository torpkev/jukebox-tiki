package work.torp.jukeboxtiki.scheduled;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.TikiJukebox;

public class PlaySong {
	public static void Run() {
		HashMap<Block, TikiJukebox> hmJB = Main.Jukeboxes;
		if (hmJB != null)
		{
			for (Entry<Block, TikiJukebox> tjb : hmJB.entrySet()) {
				TikiJukebox jb = tjb.getValue();
				if (jb != null)
				{
					if (jb.getIsActive())
					{
						if (jb.getIsPlaying() && !jb.isJukeboxPlaying())
						{
							Alert.DebugLog("PlaySong", "Run", "Jukebox ID: " + Integer.toString(jb.getJukeboxID()) + " is marked as playing but is not currently playing");
							if (jb.nearbyPlayers() != null)
							{
								if (jb.nearbyPlayers().size() > 0)
								{
									Alert.DebugLog("PlaySong", "Run", "Jukebox ID: " + Integer.toString(jb.getJukeboxID()) + " has players nearby - starting next disc");
									if (!jb.loadFirstDiscToStorage())
									{
										// unable to get disc
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
