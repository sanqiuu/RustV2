package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.data.RPlayer;
import com.sanqiu.rustv2.manager.PlayerManager;
import com.sanqiu.rustv2.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Blueprint {
    public static  boolean isBlueprint(ItemStack item){
        boolean result = false;
        if(item.getType() == Material.PAPER){
            String displayName = item.getItemMeta().getDisplayName();
            if(displayName!=null && displayName.split("-")[0].equals("蓝图")){
                result = true;
            }
        }
        return result;
    }
    public static boolean hasBlueprint(Player player, ItemStack itemStack){
        return PlayerManager.INSTANCE.get(player).hasBlueprint(itemStack);
    }

    public static void study(Player player, ItemStack itemStack){
        RPlayer rPlayer = PlayerManager.INSTANCE.get(player);
        ItemStack item = itemStack.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemStack.getItemMeta().getDisplayName().split("-")[1]);
        item.setItemMeta(itemMeta);
        if(rPlayer.hasBlueprint(item)){
            player.sendMessage( "该蓝图已被学习过");
        }else {
            ItemUtil.subPlayerItemAmount(player,itemStack,1);
            rPlayer.addBlueprint(item);
            player.sendMessage( "学习成功");
        }
    }
}