package me.KihCow.flash;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


public class Main extends JavaPlugin implements Listener {
    private Set<UUID> flashOnline = new HashSet<>();
    private Set<UUID> flashLeft = new HashSet<>();

    public void onEnable() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
        console.sendMessage(ChatColor.GREEN + "The Flash");
        console.sendMessage(ChatColor.GREEN + "Enabled");
        console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
    }

    private Map<UUID, ItemStack[]> playersArmor = new HashMap<>();
    private Map<UUID, Collection<PotionEffect>> playersEffects = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String cmdLbl, String[] args) {
        Player p = (Player) sender;
        UUID u = p.getUniqueId();
        if(cmdLbl.equalsIgnoreCase("Gravity")){
            p.setGravity(!p.hasGravity());
            return true;
        }


        if (cmdLbl.equalsIgnoreCase("test")) {
            p.sendMessage("\"" + p.getName() + "\" : \"" + u + "\"WORKED!!");
            return true;
        }
        if (cmdLbl.equalsIgnoreCase("flashon")) {
            flashOn(p);
            return true;
        }
        if (cmdLbl.equalsIgnoreCase("flashoff")) {
            flashOff(p);
            return true;
        }
        if (cmdLbl.equalsIgnoreCase("getFlashRing")) {
            ItemStack ring = new ItemStack(Material.DOUBLE_PLANT);
            ItemMeta ringMeta = ring.getItemMeta();
            ringMeta.setLocalizedName("Flash Ring");
            ring.setItemMeta(ringMeta);
            p.getInventory().addItem(ring);
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void flashOn(Player p) {
        UUID u = p.getUniqueId();
        flashOnline.add(u);
        EntityEquipment a = p.getEquipment();
        ItemStack[] armor = {a.getBoots(), a.getLeggings(), a.getChestplate(), a.getHelmet()};
        playersArmor.put(u, armor);
        ItemStack flashHead = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta flashMeta = (SkullMeta) flashHead.getItemMeta();
        //flashMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("f2474911-48a9-44bf-92c9-322ffc8c978e")));
        flashMeta.setOwner("flash");
        flashHead.setItemMeta(flashMeta);

        ItemStack flashChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack flashLegs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack flashBoots = new ItemStack(Material.GOLD_BOOTS);

        LeatherArmorMeta flashLMeta = (LeatherArmorMeta) flashChestplate.getItemMeta();
        flashLMeta.setColor(Color.RED);
        flashLMeta.setUnbreakable(true);
        flashChestplate.setItemMeta(flashLMeta);
        flashLegs.setItemMeta(flashLMeta);
        flashBoots.setItemMeta(flashLMeta);
        ItemStack[] flashArmor = {flashBoots, flashLegs, flashChestplate, flashHead};
        a.setArmorContents(flashArmor);
        playersEffects.put(u, p.getActivePotionEffects());
        for(PotionEffect potionEffect : p.getActivePotionEffects())
            p.removePotionEffect(potionEffect.getType());
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 10, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 10, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100000, 10, true));
    }

    private void flashOff(Player p) {
        UUID u = p.getUniqueId();
        flashOnline.remove(u);
        p.getEquipment().setArmorContents(playersArmor.get(u));
        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        p.removePotionEffect(PotionEffectType.FAST_DIGGING);
        for(PotionEffect potionEffect : playersEffects.get(u))
            p.addPotionEffect(potionEffect);
        p.setVelocity(p.getVelocity().normalize());
    }

    private void flashToggle(Player p) {
        if (flashOnline.contains(p.getUniqueId()))
            flashOff(p);
        else
            flashOn(p);
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) && "Flash Ring".equals(e.getItem().getItemMeta().getLocalizedName()))
            flashToggle(e.getPlayer());
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if("Flash Ring".equals(e.getItemInHand().getItemMeta().getLocalizedName()))
            e.setCancelled(true);
    }
    private boolean happened = false;
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (flashOnline.contains(p.getUniqueId())) {
            p.spawnParticle(Particle.CLOUD, p.getLocation(), 1, 0.0, 0.0, 0.0, 0.0);
            //Figuring out running on water.
            if (e.getTo().getBlock().isLiquid()) {
                /*p.setGravity(false);
                p.getLocation().setY(loc.getY() + p.getFallDistance() == 0 ? 1:0);
                p.spawnParticle(Particle.WATER_BUBBLE, p.getLocation(), 1);
                if(!happened)
                    p.setVelocity(new Vector(0.0, 0.0, 0.0));
                happened = true;*/
                ItemMeta lBoots = p.getEquipment().getBoots().getItemMeta();
                lBoots.addEnchant(Enchantment.DEPTH_STRIDER, 500, true);
                p.getEquipment().getBoots().setItemMeta(lBoots);
                happened = true;
            } else if(happened){
                ItemMeta lBoots = p.getEquipment().getBoots().getItemMeta();
                lBoots.removeEnchant(Enchantment.DEPTH_STRIDER);
                p.getEquipment().getBoots().setItemMeta(lBoots);
                happened = false;
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        if (flashOnline.contains(u)) {
            flashOff(e.getPlayer());
            flashLeft.add(u);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID u = e.getPlayer().getUniqueId();
        if (flashLeft.contains(u)) {
            flashLeft.remove(u);
            flashOn(e.getPlayer());
        }
    }

    @EventHandler
    public void onArmorSlot(InventoryClickEvent e) {
        if (e.getSlotType().equals(SlotType.ARMOR) && flashOnline.contains(e.getWhoClicked().getUniqueId()))
            e.setCancelled(true);
    }

    public void onDisable() {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
        console.sendMessage(ChatColor.RED + "The Flash");
        console.sendMessage(ChatColor.RED + "Disabled");
        console.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-");
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (flashOnline.contains(p.getUniqueId()))
                flashOff(p);
        }
    }
}