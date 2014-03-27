package me.kyledag500.UltimateHub;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	
	private static Plugin plugin;
	
	private Selector selector;
	CustomConfig selectorconfig = new CustomConfig(this, "selector.yml");
	
	private Launchpads launchpads;
	CustomConfig launchpadsconfig = new CustomConfig(this, "launchpads.yml");
	
	private Toggler Toggler;
	CustomConfig togglerconfig = new CustomConfig(this, "playertoggler.yml");
	
	String prefix = "";
	
	public void onEnable(){
		getConfig();
		getConfig().addDefault("prefix", "&f[&4Ultimate&6Hub&f]");
		getConfig().addDefault("clearInvOnJoin", "true");
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		prefix = getConfig().getString("prefix").replace("&", "§") + " ";
		
		Bukkit.getPluginManager().registerEvents(this, this);		
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
		
		setupSelector();
		setupLaunchpads();
		setupToggler();
		
	}
	
	public void setupToggler(){
		Toggler = new Toggler(this);
    	togglerconfig.createIfNoExist();
		if(!togglerconfig.getConfig().getString("enabled").equalsIgnoreCase("false")){
			Bukkit.getPluginManager().registerEvents(Toggler, this);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	            public void run() {
	    	    	Toggler.setup();;
	                  }
	          }, 5L);	
		}
	}
	
	public void setupLaunchpads(){
		launchpads = new Launchpads(this);
    	launchpadsconfig.createIfNoExist();
		if(!launchpadsconfig.getConfig().getString("enabled").equalsIgnoreCase("false")){
			Bukkit.getPluginManager().registerEvents(launchpads, this);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
	            public void run() {
	    	    	launchpads.setup();
	                  }
	          }, 5L);	
		}
	}
	
	public void setupSelector(){
		selector = new Selector(this);
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
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		if(getConfig().getString("clearInvOnJoin").equalsIgnoreCase("true")){
			event.getPlayer().getInventory().clear();
		}
	}

}
