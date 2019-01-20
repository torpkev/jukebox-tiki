package work.torp.jukeboxtiki.classes;

import org.bukkit.Material;

import work.torp.jukeboxtiki.helpers.Provide;

public class MusicDisc {
	// Private objects
	private Material disc;
	private String name;
	private int seconds;
	private int orderby;
	
	// Getters & Setters
	public Material getDisc()
	{
		return this.disc;
	}
	public void setDisc(Material disc)
	{
		this.disc = disc;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getSeconds()
	{
		return this.seconds;
	}
	public void setSeconds(int seconds)
	{
		this.seconds = seconds;
	}
	public int getOrderBy()
	{
		return this.orderby;
	}
	public void setOrderBy(int orderby)
	{
		this.orderby = orderby;
	}
	
	// Functions
	public void init()
	{
		this.disc = Material.AIR;
		this.name = "";
		this.seconds = 0;
		this.orderby = 0;
	}
	public void set(Material disc, int orderby)
	{
		this.disc = disc;
		this.name = Provide.getNameFromDisc(disc);
		this.seconds = Provide.getRecordLengthFromDisc(disc);
		this.orderby = orderby;
	}
}
