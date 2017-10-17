package me.cowslayer.first;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
	
	String consolePrefix = "[GenericPlugin]: ";
	String igPrefix = ChatColor.RED + "[§6GenericPlugin"+ChatColor.RED+"]: "+ ChatColor.WHITE;
	
	Set<UUID> flame = new HashSet<UUID>();
	
	ArrayList<UUID> playerChest = new ArrayList<UUID>();
	ArrayList<Inventory> inventoryChest = new ArrayList<Inventory>();
	
	public void onEnable() {
		System.out.println(consolePrefix + "has been enabled.");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		Player player = (Player) sender;
		if(cmdName.equalsIgnoreCase("currentxp")) {
			int level = Math.round(player.getExp()*18);
			String s = ChatColor.GREEN + "------------------".substring(0, level) + ChatColor.WHITE + "------------------".substring(level, 17);
			
			player.sendMessage(igPrefix + "\nYou, "+ player.getName() + ", are level " + player.getLevel() + ", and have\n[" + s +"] on your meter.");
			return true;
		}
		
		
		if(cmdName.equalsIgnoreCase("flameoff")) {
			flame.remove(player.getUniqueId());			
			return true;
		}
		if(cmdName.equalsIgnoreCase("micromenu")) {
			createMenu(player);
			return true;
		}	
		if(cmdName.equalsIgnoreCase("damage")) {
			player.damage(1);
			player.sendMessage("YOU HAVE BEEN dAmaGEED");
			return true;
		}
		if(cmdName.equalsIgnoreCase("god")) {
			player.setInvulnerable(!player.isInvulnerable());
			player.setHealth(20);
			player.setFoodLevel(20);
			return true;
		}
		if(cmdName.equalsIgnoreCase("heal")) {
			player.setHealth(20);
			player.setFoodLevel(20);
			player.sendMessage("H E A LS ED");
			return true;
		}
		if (cmdName.startsWith("gm")){
			GameMode[] gamemode = {GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE, GameMode.SPECTATOR};
			
			GameMode previous = player.getGameMode();
			int possibleGamemode = -1;
			if (args.length == 1 && cmdName.length() != 3) 
				possibleGamemode = Integer.parseInt(args[0]);
			if (args.length == 0 && cmdName.length() == 3)
				possibleGamemode = Integer.parseInt(cmdName.substring(2));
			if (possibleGamemode <= 3 && possibleGamemode >= 0) {
				player.setGameMode(gamemode[possibleGamemode]);
				GameMode now = player.getGameMode();
				player.sendMessage(igPrefix + ChatColor.GOLD + "Your Gamemode has been updated from " + previous + " to " + now);
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLocationChanged(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(flame.contains(p.getUniqueId()) && p.hasPotionEffect(PotionEffectType.SPEED)) {
			p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
		}
		
		
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("MicroMenu")) {
			Player player = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if(e.getSlot() == 0 && !flame.contains(player.getUniqueId())) {
				flame.add(player.getUniqueId());
				player.closeInventory();
			}
			if(e.getSlot() == 1) {
				Inventory Anvil = Bukkit.getServer().createInventory(null, InventoryType.ANVIL, "Anvil");
				player.openInventory(Anvil);
			}
			if(e.getSlot() == 2) {
				Inventory Chest = null;
				if(!playerChest.contains(player.getUniqueId())) {
					Chest = Bukkit.getServer().createInventory(null, 54, "VAULT");
				}else {
					Chest = inventoryChest.get(playerChest.indexOf(player.getUniqueId()));
				}
				player.openInventory(Chest);
			}
		}
	}
	@EventHandler
	public void onPlayerClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(!playerChest.contains(p.getUniqueId())) {
			playerChest.add(p.getUniqueId());
			inventoryChest.add(e.getInventory());
		} else {
			inventoryChest.set(playerChest.indexOf(p.getUniqueId()), e.getInventory());
		}
		
	}
	public void createMenu(Player player) {
		Inventory inv = Bukkit.getServer().createInventory(null, 9, "MicroMenu");
		
		ItemStack item1 = new ItemStack(Material.BLAZE_POWDER);
		
		ItemMeta item1Meta = item1.getItemMeta();
		
		item1Meta.setDisplayName(ChatColor.DARK_RED + "Flame");
		
		ArrayList<String> item1Lore = new ArrayList<String>();
		item1Lore.add(ChatColor.RESET + "" + ChatColor.RED + "A trail whenever you run");
		
		item1Meta.setLore(item1Lore);
		
		item1.setItemMeta(item1Meta);
		
		inv.setItem(0, item1);
		
		ItemStack item2 = new ItemStack(Material.ANVIL);
		ItemMeta item2Meta = item2.getItemMeta();
		item2Meta.setDisplayName("Portable Anvil");
		ArrayList<String> item2Lore = new ArrayList<String>();
		item2Lore.add(ChatColor.RESET + "Click for free Anvil");
		item2Meta.setLore(item2Lore);
		item2.setItemMeta(item2Meta);
		inv.setItem(1, item2);
		
		ItemStack item3 = new ItemStack(Material.CHEST);
		ItemMeta item3Meta = item3.getItemMeta();
		item3Meta.setDisplayName("Portable Chest");
		ArrayList<String> item3Lore = new ArrayList<String>();
		item3Lore.add(ChatColor.RESET + "Click for free Chest");
		item3Meta.setLore(item3Lore);
		item3.setItemMeta(item3Meta);
		inv.setItem(2, item3);
		
		
		player.openInventory(inv);
	}
	public void onDisable() {
		System.out.println(consolePrefix + "has been disabled.");
	}
}
