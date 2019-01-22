package work.torp.jukeboxtiki.helpers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;

import work.torp.jukeboxtiki.classes.MusicDisc;

public class Provide {
	public static List<Material> getDiscMaterials()
	{
		List<Material> lstM = new ArrayList<Material>();
		lstM.add(Material.MUSIC_DISC_11);
		lstM.add(Material.MUSIC_DISC_13);
		lstM.add(Material.MUSIC_DISC_BLOCKS);
		lstM.add(Material.MUSIC_DISC_CAT);
		lstM.add(Material.MUSIC_DISC_CHIRP);
		lstM.add(Material.MUSIC_DISC_FAR);
		lstM.add(Material.MUSIC_DISC_MALL);
		lstM.add(Material.MUSIC_DISC_MELLOHI);
		lstM.add(Material.MUSIC_DISC_STAL);
		lstM.add(Material.MUSIC_DISC_STRAD);
		lstM.add(Material.MUSIC_DISC_WAIT);
		lstM.add(Material.MUSIC_DISC_WARD);
		return lstM;
	}
	public static List<MusicDisc> getDiscs()
	{
		List<MusicDisc> lstD = new ArrayList<MusicDisc>();
		
		if (getDiscMaterials() != null)
		{
			int i = 0;
			for (Material m : getDiscMaterials())
			{
				MusicDisc d = new MusicDisc();
				d.init();
				d.setDisc(m);
				d.setOrderBy(i);
				lstD.add(d);
				i++;	
			}
		}
		return lstD;
	}
	public static MusicDisc getDiscByName(String discname)
	{
		MusicDisc d = new MusicDisc();
		d.init();
		Material m = Material.valueOf(discname);
		if (m != null)
		{
			d.setDisc(m);
		}
		return d;
	}
	public static Sound getSoundFromDisc(Material disc)
	{
		Sound s = null;
		switch (disc.name())
		{
			case "MUSIC_DISC_11":
				s = Sound.MUSIC_DISC_11;
				break;
			case "MUSIC_DISC_13":
				s =  Sound.MUSIC_DISC_13;
				break;
			case "MUSIC_DISC_BLOCKS":
				s =  Sound.MUSIC_DISC_BLOCKS;
				break;
			case "MUSIC_DISC_CAT":
				s =  Sound.MUSIC_DISC_CAT;
				break;
			case "MUSIC_DISC_CHIRP":
				s =  Sound.MUSIC_DISC_CHIRP;
				break;
			case "MUSIC_DISC_FAR":
				s =  Sound.MUSIC_DISC_FAR;
				break;
			case "MUSIC_DISC_MALL":
				s =  Sound.MUSIC_DISC_MALL;
				break;
			case "MUSIC_DISC_MELLOHI":
				s =  Sound.MUSIC_DISC_MELLOHI;
				break;
			case "MUSIC_DISC_STAL":
				s =  Sound.MUSIC_DISC_STAL;
				break;
			case "MUSIC_DISC_STRAD":
				s =  Sound.MUSIC_DISC_STRAD;
				break;
			case "MUSIC_DISC_WAIT":
				s =  Sound.MUSIC_DISC_WAIT;
				break;
			case "MUSIC_DISC_WARD":
				s =  Sound.MUSIC_DISC_WARD;
				break;
				default:
					break;
		}
		return s;
	}
	public static String getNameFromDisc(Material disc)
	{
		String retVal = "";
		switch (disc.name())
		{
			case "MUSIC_DISC_11":
				retVal = "Disc 11";
				break;
			case "MUSIC_DISC_13":
				retVal = "Disc 13";
				break;
			case "MUSIC_DISC_BLOCKS":
				retVal = "Blocks";
				break;
			case "MUSIC_DISC_CAT":
				retVal = "Cat";
				break;
			case "MUSIC_DISC_CHIRP":
				retVal = "Chirp";
				break;
			case "MUSIC_DISC_FAR":
				retVal = "Far";
				break;
			case "MUSIC_DISC_MALL":
				retVal = "Mall";
				break;
			case "MUSIC_DISC_MELLOHI":
				retVal = "Mellohi";
				break;
			case "MUSIC_DISC_STAL":
				retVal = "Stal";
				break;
			case "MUSIC_DISC_STRAD":
				retVal = "Strad";
				break;
			case "MUSIC_DISC_WAIT":
				retVal = "Wait";
				break;
			case "MUSIC_DISC_WARD":
				retVal = "Ward";
				break;
				default:
					break;
		}
		return retVal;
	}
	public static int getRecordLengthFromDisc(Material disc)
	{
		int s = 0;
		switch (disc.name())
		{
			case "MUSIC_DISC_11":
				s = 71;
				break;
			case "MUSIC_DISC_13":
				s =  178;
				break;
			case "MUSIC_DISC_BLOCKS":
				s =  345;
				break;
			case "MUSIC_DISC_CAT":
				s =  185;
				break;
			case "MUSIC_DISC_CHIRP":
				s =  185;
				break;
			case "MUSIC_DISC_FAR":			
				s =  174;
				break;
			case "MUSIC_DISC_MALL":
				s =  197;
				break;
			case "MUSIC_DISC_MELLOHI":
				s =  96;
				break;
			case "MUSIC_DISC_STAL":
				s =  150;
				break;
			case "MUSIC_DISC_STRAD":
				s =  188;
				break;
			case "MUSIC_DISC_WAIT":
				s =  238;
				break;
			case "MUSIC_DISC_WARD":
				s =  251;
				break;
				default:
					break;
		}
		return s;
	}
}
