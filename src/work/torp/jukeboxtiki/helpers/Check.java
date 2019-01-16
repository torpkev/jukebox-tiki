package work.torp.jukeboxtiki.helpers;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import work.torp.jukeboxtiki.alerts.Alert;

public class Check {
	public static boolean hasPermission(Player player, String permission)
	{
		if (player.isOp() || player.hasPermission(permission))
		{
			return true;
		} else {
			return false;
		}
	}
	public static boolean isMusicDisc(Material m)
	{
		boolean retVal = false;
		if (m != null)
		{
			switch (m)
			{
			case MUSIC_DISC_11:
				retVal = true;
				break;
			case MUSIC_DISC_13:
				retVal = true;
				break;	
			case MUSIC_DISC_BLOCKS:
				retVal = true;
				break;
			case MUSIC_DISC_CAT:
				retVal = true;
				break;
			case MUSIC_DISC_CHIRP:
				retVal = true;
				break;
			case MUSIC_DISC_FAR:
				retVal = true;
				break;
			case MUSIC_DISC_MALL:
				retVal = true;
				break;
			case MUSIC_DISC_MELLOHI:
				retVal = true;
				break;
			case MUSIC_DISC_STAL:
				retVal = true;
				break;
			case MUSIC_DISC_STRAD:
				retVal = true;
				break;
			case MUSIC_DISC_WAIT:
				retVal = true;
				break;
			case MUSIC_DISC_WARD:
				retVal = true;
				break;	
			default:
				break;
			}
			if (retVal)
			{
				Alert.DebugLog("Check", "isMusicDisc", "Material is a music disc - Disc: " + m.name());
			} else {
				Alert.DebugLog("Check", "isMusicDisc", "Material not a music disc - Material: " + m.name());
			}
		}
		return retVal;
	}
}
