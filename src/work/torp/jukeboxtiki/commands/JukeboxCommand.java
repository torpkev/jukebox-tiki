package work.torp.jukeboxtiki.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import work.torp.jukeboxtiki.Main;
import work.torp.jukeboxtiki.helpers.Check;
import work.torp.jukeboxtiki.alerts.Alert;
import work.torp.jukeboxtiki.classes.JukeboxBlock;

public class JukeboxCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		StringBuilder fullargs = new StringBuilder();
		int iargs = 0;
		if (args.length > 0) {
			for (Object o : args)
			{
				iargs++;
				fullargs.append(o.toString());
				fullargs.append(" ");
			}
			Alert.DebugLog("JukeboxCommands", "onCommand", "/jukebox command run by " + sender.getName() + " with arguments: " + fullargs);
		}
		if (iargs > 0)
		{
			if (args[0] != null)
			{
				boolean hasPerm = false;
				if (sender.getName() == "CONSOLE")
				{
					hasPerm = true;
				}
				
				if (sender instanceof Player)
				{
					Player player = (Player) sender;
					if (Check.hasPermission(player, "jukebox.save"))
					{
						hasPerm = true;
					}
				} 
				if (hasPerm)
				{
					if (args[0].toString().equals("save"))
					{
				    	HashMap<Block, JukeboxBlock> hmJB = Main.JukeboxBlocks; // get the JukeboxBlock entries
						if (hmJB != null) // make sure the value isn't null
						{
							for (Entry<Block, JukeboxBlock> jbb : hmJB.entrySet()) { // loop through JukeboxBlock entries
								Main.getInstance().getDatabase().saveJukebox(jbb.getValue());
							}
							Alert.Sender("Jukebox entries saved", sender, true);
						}
					} else {
						Alert.Sender("Usage: /jukebox save", sender, true);
					}
				} else {
					Alert.Sender("You do not have permission to use /jukebox", sender, true);
				}
			}	
		} else {
			Alert.Sender("Usage: /jukebox save", sender, true);
		}
		return true;
	}

}
