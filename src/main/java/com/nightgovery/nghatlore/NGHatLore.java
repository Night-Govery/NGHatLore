package com.nightgovery.nghatlore;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class NGHatLore extends JavaPlugin {
    private String limit_lore;
    private String no_item_message;
    private String no_lore_message;
    private String on_hold_message;
    private String no_permission_message;
    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfig();
        Bukkit.getConsoleSender().sendMessage("§cNGHatLore加载成功！");
        Bukkit.getConsoleSender().sendMessage("§c版本:V0.0.1");
        Bukkit.getConsoleSender().sendMessage("§c作者:夜政");
        Bukkit.getConsoleSender().sendMessage("§cQQ: §e208989395");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("§cNGHatLore卸载成功！");
    }
    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();

        limit_lore = config.getString("limit-lore", "可穿戴");
        no_item_message = config.getString("no-item-message", "§c你必须手持物品才能使用此指令");
        no_lore_message = config.getString("no-lore-message", "§c该物品不可穿戴");
        on_hold_message = config.getString("on-hold-message", "§a穿戴成功");
        no_permission_message = config.getString("no-permission-message", "§c你没有权限这么做");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length >= 1 && sender.isOp() && args[0].equals("reload")) {
            loadConfig();
            sender.sendMessage("§6§lNGHatLore重载配置成功！");
            return true;
        }

        if(command.getName().equals("nghat")) {
            Player player = (Player) sender;
            if (player.hasPermission("nghatlore.nghat")) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                int amount = player.getInventory().getItemInMainHand().getAmount();
                if (amount == 0) {
                    player.sendMessage(no_item_message);
                } else {
                    ItemMeta meta = hand.getItemMeta();
                    if(meta.getLore() != null){
                        List<String> lorelist = meta.getLore();
                        for(int x=0;x<lorelist.size();x++){
                            String temp = lorelist.get(x);
                            if(temp.contains(limit_lore)){
                                player.getInventory().setItemInMainHand(hand);
                                int afterhat = amount - 1;
                                player.getInventory().getItemInMainHand().setAmount(afterhat);
                                player.getInventory().setHelmet(hand);
                                player.sendMessage(on_hold_message);
                                break;
                            }
                            if(x+1 == lorelist.size()){
                                player.sendMessage(no_lore_message);
                            }
                        }
                    }else{
                        player.sendMessage(no_lore_message);
                    }
                }
            }else{
                player.sendMessage(no_permission_message);
            }
        }
        return true;
    }
}
