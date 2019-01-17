package work.torp.jukeboxtiki.classes;

import java.util.UUID;

import org.bukkit.Material;

public class Disc {
	private Material disc;
	private int orderby;
	private UUID shuffleuuid;
	
	public Material getDisc()
	{
		return this.disc;
	}
	public void setDisc(Material disc)
	{
		this.disc = disc;
	}
	public int getOrderBy()
	{
		return this.orderby;
	}
	public void setOrderBy(int orderby)
	{
		this.orderby = orderby;
	}
	public UUID getShuffleUUID()
	{
		return this.shuffleuuid;
	}
	public void setShuffleUUID()
	{
		this.shuffleuuid = UUID.randomUUID();
	}
	public void init()
	{
		this.disc = Material.AIR;
		this.orderby = -1;
		this.shuffleuuid = UUID.randomUUID();
	}
	public void set(Material disc, int orderby)
	{
		this.disc = disc;
		this.orderby = orderby;
	}
}
