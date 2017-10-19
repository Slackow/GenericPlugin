package me.KihCow.flash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
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
  public Main() {}
  
  Set<UUID> Lightning = new HashSet<UUID>();
  
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
    Player player = (Player)sender;
    if (commandLabel.equalsIgnoreCase("flashon")) {
      Lightning.add(player.getUniqueId());
      EntityEquipment a = player.getEquipment();
      ItemStack[] armor = {a.getBoots(), a.getLeggings(), a.getChestplate(), a.getHelmet()};
      players.put(player.getUniqueId(), armor);
      	ItemStack flashHead = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
      	SkullMeta flashMeta = (SkullMeta) flashHead.getItemMeta();
      	flashMeta.setOwner("Barry_H_Allen");
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
      player.getEquipment().setArmorContents(flashArmor);
      player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 10, true));
     return true;
    }
       if (commandLabel.equalsIgnoreCase("flashoff")) {
         Lightning.remove(player.getUniqueId());
         player.getEquipment().setArmorContents(players.get(player.getUniqueId()));
     	 player.removePotionEffect(PotionEffectType.SPEED);
        return true;
        }
    return false;
  }
  
@SuppressWarnings("deprecation")
@EventHandler
  public void onPlayerMove(PlayerMoveEvent e)
  {
	Player p = e.getPlayer();
    if (Lightning.contains(p.getUniqueId()) && p.hasPotionEffect(PotionEffectType.SPEED)) 
    	p.playEffect(p.getLocation(), Effect.CLOUD, 1);
  }

@EventHandler
public void onArmorSlot(InventoryClickEvent event) {
    if (event.getSlotType() == SlotType.ARMOR  && Lightning.contains(event.getWhoClicked().getUniqueId())){
        event.setCancelled(true);
    }
}
  
  public void onDisable() {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
    console.sendMessage(ChatColor.RED + "The Flash");
    console.sendMessage(ChatColor.RED + "Disabled");
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
  }
}