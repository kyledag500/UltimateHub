package me.kyledag500.UltimateHub;

import java.util.ArrayList;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	
	Boolean updated = false;
	Updater updater = null;
	
	public void onEnable(){
		getConfig();
		getConfig().addDefault("prefix", "&f[&4Ultimate&6Hub&f]");
		getConfig().addDefault("clearInvOnJoin", "true");
		getConfig().addDefault("autoUpdate", "true");
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix")) + " ";
		
		Bukkit.getPluginManager().registerEvents(this, this);		
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
		
		setupSelector();
		setupLaunchpads();
		setupToggler();
		
		if(getConfig().getString("autoUpdate").equalsIgnoreCase("true")){
			updater = new Updater(this, 76973, this.getFile(), Updater.UpdateType.DEFAULT, true);
			Bukkit.broadcastMessage(updater.getLatestName());
		}
		else{
			Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "You have disabled the UltimateHub auto updater. This is not recomended. You can re-enable it from the config.");
		}
		
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
		if(event.getPlayer().hasPermission("update.inform")){
	        Updater.UpdateResult result = updater.getResult();
	        switch(result){
	        	case SUCCESS: event.getPlayer().sendMessage(prefix + ChatColor.GREEN + "UltimateHub has downloaded a new update: " + ChatColor.RED + updater.getLatestName());
	        				event.getPlayer().sendMessage(prefix + ChatColor.GREEN + "It will be loaded next time the server is reloaded/restarted.");
	                break;
	            case DISABLED:
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "UltimateHub has update " + ChatColor.RED + updater.getLatestName() + " available for download, but you disabled the auto-downloader.");
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "Please download the newest update ASAP from " + ChatColor.DARK_PURPLE + updater.getLatestFileLink());
	                break;
	            case FAIL_DOWNLOAD:
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "UltimateHub has a new update (" + ChatColor.DARK_PURPLE + updater.getLatestName() + ") available, but we were unable to download it.");
	                break;
	            case FAIL_DBO:
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "UltimateHub has a new update (" + ChatColor.DARK_PURPLE + updater.getLatestName() + ") available, but we were unable to connect to DBO it.");
	                break;
	            case FAIL_NOVERSION:
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "UltimateHub has a new update (" + ChatColor.DARK_PURPLE + updater.getLatestName() + ") available, but we were unable to download it, do to a version naming issue.");
	                break;
	            case FAIL_BADID:
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "Updater contains a bad ID for UltimateHub.");
	                break;
	            case FAIL_APIKEY:
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "UltimateHub has a new update (" + ChatColor.DARK_PURPLE + updater.getLatestName() + ") available, but we were unable to download it.");
    				event.getPlayer().sendMessage(prefix + ChatColor.RED + "Please verify your API Key is correct.");
	                break;
	            case UPDATE_AVAILABLE:
	              // There was an update found, but because you had the UpdateType set to NO_DOWNLOAD, it was not downloaded.			
	        }
		}
	}

}
