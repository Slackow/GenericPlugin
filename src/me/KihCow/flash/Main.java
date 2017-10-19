package me.KihCow.flash;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
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
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    Player player = (Player)sender;
    ItemStack Helmet = player.getEquipment().getHelmet();
    ItemStack Chest = player.getEquipment().getChestplate();
    ItemStack Legs = player.getEquipment().getLeggings();
    ItemStack Boots = player.getEquipment().getBoots();
    if (commandLabel.equalsIgnoreCase("flashon")) {
      Lightning.add(player.getUniqueId());
      player.getEquipment().setHelmet(new ItemStack(Material.SKULL));
     return true;
    }
       if (commandLabel.equalsIgnoreCase("flashoff")) {
         Lightning.remove(player.getUniqueId());
         player.getEquipment().setHelmet(new ItemStack(Helmet));
         player.getEquipment().setChestplate(new ItemStack(Chest));
         player.getEquipment().setLeggings(new ItemStack(Legs));
         player.getEquipment().setBoots(new ItemStack(Boots));
        return true;
        }
    return false;
  }
  
@SuppressWarnings("deprecation")
@EventHandler
  public void onPlayerMove(PlayerMoveEvent e)
  {
	Player p = e.getPlayer();
    if (Lightning.contains(e.getPlayer().getUniqueId()) && p.hasPotionEffect(PotionEffectType.SPEED)) {}
    	e.getPlayer().playEffect(e.getPlayer().getLocation(), Effect.MAGIC_CRIT, 10);
  }
  
  public void onDisable() {
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
    console.sendMessage(ChatColor.RED + "The Flash");
    console.sendMessage(ChatColor.RED + "Disabled");
    console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
  }
}