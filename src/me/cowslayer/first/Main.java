package me.cowslayer.first;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	String consolePrefix = "[Vault]: ";
	String igPrefix = ChatColor.RED + "[§6Vault"+ChatColor.RED+"]: "+ ChatColor.WHITE;
	
	Map<UUID, Inventory[]> vaults = new HashMap<>();
	
	public void onEnable() {
		System.out.println(consolePrefix + "has been enabled.");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		Player player = (Player) sender;
		if(cmdName.equalsIgnoreCase("vault")) {
			createMenu(player);
			return true;
		}
		if(cmdName.equalsIgnoreCase("echest")) {
			player.openInventory(player.getEnderChest());
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onInvClicked(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		UUID u = p.getUniqueId();
		Inventory inv = e.getInventory();
		if(inv.getName().equals("Vaults")) {
			e.setCancelled(true);
			if(!vaults.containsKey(u)) {
				Inventory[] emptyInvs = new Inventory[18];
				for(int i = 0;i < 18;i++) {
					emptyInvs[i] = Bukkit.getServer().createInventory(null, 54, "Vault " + (i + 1));
				}
				vaults.put(u, emptyInvs);
				
			} 
			p.openInventory(vaults.get(u)[e.getSlot()]);
		}
	}
	
	
	
	@EventHandler
	public void onPlayerClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		UUID u = p.getUniqueId();
		Inventory inv = e.getInventory();
		if(inv.getName().startsWith("Vault ")) {
			Inventory[] invs = vaults.get(u);
			invs[Integer.parseInt(getName().substring(6))-1] = inv;
			vaults.put(u, invs);
			
		}
	}
	public void createMenu(Player player) {
		Inventory inv = Bukkit.getServer().createInventory(null, 18, "Vaults");
		for(int i = 0;i < 18;i++) {
			ItemStack item = new ItemStack(Material.CHEST);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setLocalizedName("Vault " + (i + 1) );
			item.setItemMeta(itemMeta);
			inv.setItem(i, item);
		}
		player.openInventory(inv);
	}
	public void onDisable() {
		System.out.println(consolePrefix + "has been disabled.");
	}
}
