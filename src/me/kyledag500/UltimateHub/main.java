package me.kyledag500.UltimateHub;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	
	private static Plugin plugin;
	
	private Selector selector;
	CustomConfig selectorconfig = new CustomConfig(this, "selector.yml");
	String prefix = "";
	
	public void onEnable(){
		getConfig();
		getConfig().addDefault("prefix", "&f[&4Ultimate&6Hub&f]");
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		prefix = getConfig().getString("prefix").replace("&", "§") + " ";
		
		Bukkit.getPluginManager().registerEvents(this, this);		
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
		
		selector = new Selector(this);
		setupSelector();
		
	}
	
	public void setupSelector(){
    	selectorconfig.createIfNoExist();
		if(!selectorconfig.getConfig().getString("enabled").equalsIgnoreCase("false")){
			Bukkit.getPluginManager().registerEvents(selector, this);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	            public void run() {
	    	    	selector.setupSelector();
	                  }
	          }, 5L);	
		}
	}

}
