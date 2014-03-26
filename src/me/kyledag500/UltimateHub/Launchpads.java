package me.kyledag500.UltimateHub;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class Launchpads implements Listener{

	main main;
	public Plugin thisplugin;
	CustomConfig launchpads = null;
	ArrayList<Player> active = new ArrayList<Player>();

	public Launchpads(main plugin){
		this.main = plugin;
		launchpads = main.launchpadsconfig;
	}
	
	public void setup(){
		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		final Player player = event.getPlayer();
		if(player.getLocation().getBlock().getType().toString().equals(launchpads.getConfig().getString("main").toUpperCase())){
			if(player.getLocation().subtract(0, 1, 0).getBlock().getType().toString().equals(launchpads.getConfig().getString("first").toUpperCase())){
				if(player.getLocation().subtract(0, 2, 0).getBlock().getType().toString().equals(launchpads.getConfig().getString("second").toUpperCase())){
					if(!active.contains(player)){
						if(!launchpads.getConfig().getString("sound").equalsIgnoreCase("none")){
							player.playSound(player.getLocation(), Sound.valueOf(launchpads.getConfig().getString("sound").toUpperCase()), 1, 1);	
						}
						if(!launchpads.getConfig().getString("effect").equalsIgnoreCase("none")){
							player.playEffect(player.getLocation(), Effect.valueOf(launchpads.getConfig().getString("effect").toUpperCase()), 1);						
						}
						player.setVelocity(player.getLocation().getDirection().multiply(Double.valueOf(launchpads.getConfig().getString("multiply"))));
						player.setVelocity(new Vector(player.getVelocity().getX(), Integer.parseInt(launchpads.getConfig().getString("upward")), player.getVelocity().getZ()));
						active.add(player);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				            public void run() {
				    	    	active.remove(player);
				                  }
				          }, 5L);	
					}
				}
			}
		}
	}
	
}
