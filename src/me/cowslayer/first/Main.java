package me.cowslayer.first;

import java.util.ArrayList;
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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener{
	
	String consolePrefix = "[GenericPlugin]: ";
	String igPrefix = ChatColor.RED + "[§6GenericPlugin"+ChatColor.RED+"]: "+ ChatColor.WHITE;
	
	ArrayList<UUID> Flame = new ArrayList<UUID>();
	
	
	
	
	
	public void onEnable() {
		System.out.println(consolePrefix + "has been enabled.");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(this, this);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if(commandLabel.equalsIgnoreCase("currentxp")) {
			int level = Math.round(player.getExp()*18);
			String s = ChatColor.GREEN + "------------------".substring(0, level) + ChatColor.WHITE + "------------------".substring(level, 17);
			
			player.sendMessage(igPrefix + "\nYou, "+ player.getName() + ", are level " + player.getLevel() + ", and have\n[" + s +"] on your meter.");
			return true;
		}
		if(commandLabel.equalsIgnoreCase("flameoff")) {
			Flame.remove(player.getUniqueId());
			return true;
		}
		
		
		if(commandLabel.equalsIgnoreCase("damage")) {
			player.damage(1);
			player.sendMessage("YOU HAVE BEEN dAmaGEED");
			return true;
		}
		if(commandLabel.equalsIgnoreCase("god")) {
			player.setInvulnerable(!player.isInvulnerable());
			player.setHealth(20);
			player.setFoodLevel(20);
			return true;
		}
		if(commandLabel.equalsIgnoreCase("heal")) {
			player.setHealth(20);
			player.setFoodLevel(20);
			player.sendMessage("H E A LS ED");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("chest")) {
			createMenu(player);
			return true;
		}
		if (commandLabel.startsWith("gm")){
			GameMode[] gamemode = {GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE, GameMode.SPECTATOR};
			
			GameMode previous = player.getGameMode();
			int possibleGamemode = 0;
			if (args.length == 1) 
				possibleGamemode = Integer.parseInt(args[0]);
			if (args.length == 0 && commandLabel.length() == 3)
				possibleGamemode = Integer.parseInt(commandLabel.substring(2));
			if (possibleGamemode <= 3 && possibleGamemode >= 0) {
				player.setGameMode(gamemode[possibleGamemode]);
				GameMode now = player.getGameMode();
				player.sendMessage(igPrefix + ChatColor.GOLD + "Your Gamemode has been updated from " + previous + " to " + now);
			} else {
				player.sendMessage(ChatColor.GOLD + "Wrong Number IDIOT");
			}
			return true;
		}
		return false;
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLocationChanged(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(Flame.contains(p.getUniqueId()) && p.hasPotionEffect(PotionEffectType.SPEED)) {
			p.playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
		}
		
		
	}
	
	
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("CHEST IN A CHEST")) {
			Player player = (Player) e.getWhoClicked();
			player.sendMessage("CLICKED ON CHEST LOLOLOL");
			e.setCancelled(true);
			Flame.add(player.getUniqueId());
			player.closeInventory();
		}
	}
	
	public void createMenu(Player player) {
		Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.CHEST, "CHEST IN A CHEST");
		
		ItemStack item1 = new ItemStack(Material.CHEST);
		
		ItemMeta item1Meta = item1.getItemMeta();
		
		item1Meta.setDisplayName(ChatColor.DARK_RED + "INCEPTION");
		
		ArrayList<String> item1Lore = new ArrayList<String>();
		item1Lore.add(ChatColor.RESET + "BWAAAAAA");
		
		item1Meta.setLore(item1Lore);
		
		item1.setItemMeta(item1Meta);
		
		inv.setItem(0, item1);
		
		player.openInventory(inv);
	}
	
	
	
	public void onDisable() {
		System.out.println(consolePrefix + "has been disabled.");
	}
}
