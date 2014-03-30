package me.kyledag500.UltimateHub;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;





import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Selector implements Listener{

	main main;
	public Plugin thisplugin;
	CustomConfig selector = null;
	ItemStack item = null;
	EnchantGlow glow = null;

	public Selector(main plugin){
		this.main = plugin;
		selector = main.selectorconfig;
	}
	
	public void setupSelector(){
		addEnchants();
    	String[] type = selector.getConfig().getString("selector.type").split(":");
    	item = new ItemStack(Material.valueOf(type[0].toUpperCase()), 1, Short.parseShort(type[1]));
    	ItemMeta im = item.getItemMeta();
    	im.setDisplayName(ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString("selector.displayName")));
    	ArrayList<String> lore = new ArrayList<String>();
    	for(String l : selector.getConfig().getStringList("selector.lore")){
    		lore.add(ChatColor.translateAlternateColorCodes('&', l));
    	}
    	im.setLore(lore);
    	item.setItemMeta(im);
	}
	public void addEnchants() {
		glow = new EnchantGlow(120);
		try {
		    Field f = Enchantment.class.getDeclaredField("acceptingNew");
		    f.setAccessible(true);
		    f.set(null, true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		try {
			EnchantmentWrapper.registerEnchantment(glow);
		} catch (IllegalArgumentException e){
			 Bukkit.getConsoleSender().sendMessage("[UltimateHub] Although it is not recomended you reload...I will attempt to operate properly.");
		}
	}
	
    public void teleToServer(Player player, String msg, String server) {
        player.sendMessage(msg);
        try {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                out.writeUTF("Connect");
                out.writeUTF(server);
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
                b.close();
                out.close();
        } catch (Exception e) {}
}
	
	
	
	
	
	
	public void giveSelector(Player player){
		player.getInventory().setItem(selector.getConfig().getInt("giveSlot"), item);
	}
	
	public void openMenu(Player player){
		Inventory main = Bukkit.createInventory(null, Integer.parseInt(selector.getConfig().getString("menuSize")), ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString("menuTitle")));
		for(String i : selector.getConfig().getStringList("items")){
			String[] type = selector.getConfig().getString(i + ".type").split(":");
			ItemStack item = new ItemStack(Material.valueOf(type[0].toUpperCase()), selector.getConfig().getInt(i + ".amount"), Short.parseShort(type[1]));
			ItemMeta im = item.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString(i + ".displayName")));
	    	ArrayList<String> lore = new ArrayList<String>();
	    	for(String l : selector.getConfig().getStringList(i + ".lore")){
	    		lore.add(ChatColor.translateAlternateColorCodes('&', l));
	    	}
	    	im.setLore(lore);
	    	item.setItemMeta(im);
	    	if(!selector.getConfig().getString(i + ".glow").equalsIgnoreCase("false")){
	    		item.addUnsafeEnchantment(glow, 1);
	    	}
	    	main.setItem(Integer.parseInt(selector.getConfig().getString(i + ".slot")), item);
		}
		player.openInventory(main);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event){
		if(event.getWhoClicked() instanceof Player){
			Player player = (Player) event.getWhoClicked();
			if(event.getInventory() != null){
				Inventory inv = event.getInventory();
				if(inv.getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString("menuTitle")))){
					event.setCancelled(true);
					if(event.getCurrentItem() != null){
						ItemStack citem = event.getCurrentItem();
						for(String i : selector.getConfig().getStringList("items")){
							String[] type = selector.getConfig().getString(i + ".type").split(":");
							ItemStack item = new ItemStack(Material.valueOf(type[0].toUpperCase()), selector.getConfig().getInt(i + ".amount"), Short.parseShort(type[1]));
							ItemMeta im = item.getItemMeta();
							im.setDisplayName(ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString(i + ".displayName")));
					    	ArrayList<String> lore = new ArrayList<String>();
					    	for(String l : selector.getConfig().getStringList(i + ".lore")){
					    		lore.add(ChatColor.translateAlternateColorCodes('&', l));
					    	}
					    	im.setLore(lore);
					    	item.setItemMeta(im);
					    	if(!selector.getConfig().getString(i + ".glow").equalsIgnoreCase("false")){
					    		item.addUnsafeEnchantment(glow, 1);
					    	}
					    	if(citem.isSimilar(item)){
					    		teleToServer(player, main.prefix + ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString(i + ".message")), ChatColor.translateAlternateColorCodes('&', selector.getConfig().getString(i + ".server")));
					    	}
						}
					}
					
				}
			}
		}
	}
	
	
	
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            public void run() {
            	giveSelector(player);
                  }
          }, 5L);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
        if ((event.getAction() != Action.PHYSICAL) && (player.getItemInHand().isSimilar(item))){
        	event.setCancelled(true);
        	openMenu(player);
        }
	}
}
