package me.kyledag500.UltimateHub;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class General implements Listener{
	main Main;
	Plugin thisplugin;
	FileConfiguration config;
	CustomConfig players;
	Location spawn;
	
	public General(Plugin plugin, main main){
		Main = main;
		thisplugin = plugin;
		players = main.generalplayers;
		config = thisplugin.getConfig();
		config.addDefault("prefix", "&f[&4Ultimate&6Hub&f]");
		config.addDefault("clearInvOnJoin", "true");
		config.addDefault("autoUpdate", "true");
		
		config.addDefault("spawn.tpOnJoin", "true");
		config.addDefault("spawn.command", "true");
		config.addDefault("spawn.world", "world");
		config.addDefault("spawn.x", "0");
		config.addDefault("spawn.y", "100");
		config.addDefault("spawn.z", "0");
		config.addDefault("spawn.pitch", "0");
		config.addDefault("spawn.yaw", "0");
		
		config.addDefault("welcomeMessage.enabled", "true");
		config.addDefault("welcomeMessage.message", "&c%player% &ahas joined the server for the first time! Say hello!");
		
		config.addDefault("joinMessage.enabled", "false");
		config.addDefault("joinMessage.message", "&aHey &c%player%&a! This is the message everyone sees when someone joins.");
		config.addDefault("leaveMessage.enabled", "false");
		config.addDefault("leaveMessage.message", "&aHey &c%player%&a! This is the message everyone sees when someone leaves.");
		
		ArrayList<String> motd = new ArrayList<String>();
		motd.add("&a&m===================");
		motd.add("&4Welcome to MyServer, &c%player%&4!");
		motd.add("&6We are running UltimateHub by &ckyledag500!");
		motd.add("&aWe now have prison!");
		motd.add("&a&m===================");
		config.addDefault("motd.enabled", "true");
		config.addDefault("motd.message", motd);
		
		config.options().copyDefaults(true);
		thisplugin.saveConfig();
		
		World world = Bukkit.getWorld(config.getString("spawn.world"));
		Double x = Double.parseDouble(config.getString("spawn.x"));
		Double y = Double.parseDouble(config.getString("spawn.y"));
		Double z = Double.parseDouble(config.getString("spawn.z"));
		Float pitch = Float.valueOf(config.getString("spawn.pitch"));
		Float yaw = Float.valueOf(config.getString("spawn.yaw"));
		spawn = new Location(world, x, y, z, pitch, yaw);      
	}
	
	@EventHandler
	public void onKickLeave(PlayerKickEvent event){
		Player player = event.getPlayer();
		if(config.getString("leaveMessage.enabled").equalsIgnoreCase("true")){
			event.setLeaveMessage(ChatColor.translateAlternateColorCodes('&', config.getString("leaveMessage.message")).replace("%player%", player.getName()));
		}
		else{
			event.setLeaveMessage(null);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(config.getString("leaveMessage.enabled").equalsIgnoreCase("true")){
			event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', config.getString("leaveMessage.message")).replace("%player%", player.getName()));
		}
		else{
			event.setQuitMessage(null);
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		if(config.getString("joinMessage.enabled").equalsIgnoreCase("true")){
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', config.getString("joinMessage.message")).replace("%player%", player.getName()));
		}
		else{
			event.setJoinMessage(null);
		}
		if(players.getConfig().getString(player.getUniqueId().toString()) == null){
			if(config.getString("welcomeMessage.enabled").equalsIgnoreCase("true")){
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("welcomeMessage.message")).replace("%player%", player.getName()));
			}	
		}
		players.getConfig().set(event.getPlayer().getUniqueId().toString() + ".username", player.getName());
		players.saveConfig();
		if(config.getString("clearInvOnJoin").equalsIgnoreCase("true")){
			event.getPlayer().getInventory().clear();
		}
		if(config.getString("spawn.tpOnJoin").equalsIgnoreCase("true")){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(thisplugin, new Runnable() {
	            public void run() {
	            	player.teleport(spawn);
	                  }
	          }, 5L);
		}
		if(config.getString("motd.enabled").equalsIgnoreCase("true")){
			for(String s : config.getStringList("motd.message")){
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replace("%player%", player.getName()));
			}
		}
	}

}
