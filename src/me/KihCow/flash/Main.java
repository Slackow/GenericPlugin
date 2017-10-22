package me.KihCow.flash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener
{
  
  Set<UUID> flashOn = new HashSet<UUID>();
  
  public void onEnable() {
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
    console.sendMessage(ChatColor.GREEN + "The Flash");
    console.sendMessage(ChatColor.GREEN + "Enabled");
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
  }
  
  public String IF(String one, boolean conditional, String two) {
    if (conditional)
      return one;
    return two;
  }
  
  Map<UUID, ItemStack[]> players = new HashMap<>();
  
@SuppressWarnings("deprecation")
public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player player = (Player) sender;
    UUID u = player.getUniqueId();
    if (commandLabel.equalsIgnoreCase("flashon")) {
      flashOn.add(u);
      EntityEquipment a = player.getEquipment();
     
      ItemStack[] armor = {a.getBoots(), a.getLeggings(), a.getChestplate(), a.getHelmet()};
      players.put(u, armor);
      	ItemStack flashHead = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
      	SkullMeta flashMeta = (SkullMeta) flashHead.getItemMeta();
      	//TODO fix deprication
      	flashMeta.setOwningPlayer(Bukkit.getOfflinePlayer("flash"));
      	flashHead.setItemMeta(flashMeta);
      	ItemStack flashChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
      	ItemStack flashLegs = new ItemStack(Material.LEATHER_LEGGINGS);
      	ItemStack flashBoots = new ItemStack(Material.LEATHER_BOOTS);
      	LeatherArmorMeta flashLMeta = (LeatherArmorMeta) flashChestplate.getItemMeta();
      	flashLMeta.setColor(Color.RED);
      	flashChestplate.setItemMeta(flashLMeta);
      	flashLegs.setItemMeta(flashLMeta);
      	flashBoots.setItemMeta(flashLMeta);
      	ItemStack[] flashArmor = {flashBoots, flashLegs, flashChestplate, flashHead};
      	a.setArmorContents(flashArmor);
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 10, true));
     return true;
    }
       if (commandLabel.equalsIgnoreCase("flashoff")) {
        flashOff(player);
        return true;
        }
    return false;
  }

public void flashOff(Player p) {
	flashOn.remove(p.getUniqueId());
    p.getEquipment().setArmorContents(players.get(p.getUniqueId()));
	 p.removePotionEffect(PotionEffectType.SPEED);
}
  
@EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
	Player p = e.getPlayer();
    if (flashOn.contains(p.getUniqueId()) && p.hasPotionEffect(PotionEffectType.SPEED)) {
    	p.spawnParticle(Particle.CLOUD, p.getLocation(), 1, 0.0, 0.0, 0.0, 0.0);
    }
  }

@EventHandler
public void onArmorSlot(InventoryClickEvent event) {
    if (event.getSlotType().equals(SlotType.ARMOR)  && flashOn.contains(event.getWhoClicked().getUniqueId())){
        event.setCancelled(true);
    }
}
  
  public void onDisable() {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
    console.sendMessage(ChatColor.RED + "The Flash");
    console.sendMessage(ChatColor.RED + "Disabled");
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
    for(Player p : Bukkit.getOnlinePlayers()) {
    	if(flashOn.contains(p.getUniqueId())) 
    		flashOff(p);
    }
  }
}