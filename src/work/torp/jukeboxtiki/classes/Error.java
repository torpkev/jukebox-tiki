package work.torp.jukeboxtiki.classes;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import work.torp.jukeboxtiki.alerts.Alert;

public class Error {
	public enum Severity 
    { 
		CONTINUE,
		WARN,
		URGENT,
		CRITICAL,
		DEBUG_ONLY
    }
	private String code;
	private String source;
	private String function;
	private String friendlyFunction;
	private String summary;
	private String detail;
	private Severity severity;
	private boolean msgPlayer;
	private String playerMsg;
	private UUID uuid;
	
	public String getCode()
	{
		return this.code;
	}
	public void setCode(String code)
	{
		this.code = code;
	}
	public String getSource() 
	{
		return this.source;
	}
	public void setSource(String source)
	{
		this.source = source;
	}
	public String getFunction() 
	{
		return this.function;
	}
	public void setFunction(String function)
	{
		this.function = function;
	}
	public String getFriendlyFunction() 
	{
		return this.friendlyFunction;
	}
	public void setFriendlyFunction(String friendlyFunction)
	{
		this.friendlyFunction = friendlyFunction;
	}
	public String getSummary() 
	{
		return this.summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getDetail() 
	{
		return this.detail;
	}
	public void setDetail(String detail)
	{
		this.detail = detail;
	}
	public Severity getSeverity()
	{
		return this.severity;
	}
	public void setSeverity(Severity severity)
	{
		this.severity = severity;
	}
	public UUID getUUID()
	{
		return this.uuid;
	}
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}
	public boolean getMsgPlayer()
	{
		return this.msgPlayer;
	}
	public void setMsgPlayer(boolean msgPlayer)
	{
		this.msgPlayer = msgPlayer;
	}
	public String getPlayerMessage() 
	{
		return this.playerMsg;
	}
	public void setPlayerMessage(String playerMsg)
	{
		this.playerMsg = playerMsg;
	}
	
	public void init()
	{
		this.setCode("");
		this.setSource("");
		this.setFunction("");
		this.setFriendlyFunction("");
		this.setSummary("");
		this.setDetail("");
		this.setSeverity(Severity.CONTINUE);
		this.setUUID(UUID.randomUUID());
		this.setMsgPlayer(false);
		this.setPlayerMessage("");
	}
	public void set(String code, String source, String function, String friendlyFunction, String summary, String detail, Severity severity, UUID uuid, boolean msgPlayer, String playerMsg)
	{
		this.setCode(code);
		this.setSource(source);
		this.setFunction(function);
		this.setFriendlyFunction(friendlyFunction);
		this.setSummary(summary);
		this.setDetail(detail);
		this.setSeverity(severity);
		this.setUUID(uuid);
		this.setMsgPlayer(msgPlayer);
		this.setPlayerMessage(playerMsg);
	}
	public void recordError()
	{
		if (msgPlayer)
		{
			if (uuid != null)
			{
				Player player = Bukkit.getPlayer(uuid);
				if (player != null)
				{
					String msg = this.getPlayerMessage();
					msg = msg.replace("<%player%>", player.getDisplayName());
					Alert.Player(this.getPlayerMessage(), player, true);
				}
			}
		}
		if (this.getSeverity() != Severity.CONTINUE)
		{
			Alert.Log(this.getFriendlyFunction(), "Error reported: " + this.getSummary());
		}
		Alert.VerboseLog(this.getSource() + "." + this.getFunction(), "ERROR: Severity = " + this.getSeverity().toString() + " Summary: " + this.getSummary());
		Alert.DebugLog(this.getSource(), this.getFunction(), "ERROR: Code = " + this.getCode()  + " Severity = " + this.getSeverity().toString() + " Summary: " + this.getSummary() + " Detail: " + this.getDetail());
	}
}
