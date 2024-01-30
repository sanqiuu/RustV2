package com.sanqiu.rustv2.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemUtil {
    public static int getPlayerItemAmount(Player player , ItemStack targetItem)
    {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(targetItem)) {
                count = count + item.getAmount();
            }
        }
        return count;
    }
    public static int getPlayerItemAmount(Player player ,String targetItemName)
    {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                String DisplayName =  item.getItemMeta().getDisplayName();
                if(DisplayName!= null && DisplayName.equals(targetItemName)){
                    count = count + item.getAmount();
                }

            }
        }
        return count;
    }
    public static int getPlayerItemAmount(Inventory inventory , String targetItemName)
    {
        int count = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                String DisplayName =  item.getItemMeta().getDisplayName();
                if(DisplayName!= null && DisplayName.equals(targetItemName)){
                    count = count + item.getAmount();
                }

            }
        }
        return count;
    }
    public static boolean isPlayerItemEnough(Player player , ItemStack targetItem,int num)
    {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(targetItem)) {
                count = count + item.getAmount();
            }
        }
        return count>=num;
    }
    public static boolean isPlayerItemEnough(Player player , String targetItemName,int num)
    {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null) {
                String DisplayName =  item.getItemMeta().getDisplayName();
                if(DisplayName!= null && DisplayName.equals(targetItemName)){
                    count = count + item.getAmount();
                }

            }
        }
        return count>=num;
    }
    public static boolean isInventoryItemEnough(Inventory inventory , String targetItemName,int num)
    {
        int count = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                String DisplayName =  item.getItemMeta().getDisplayName();
                if(DisplayName!= null && DisplayName.equals(targetItemName)){
                    count = count + item.getAmount();
                }

            }
        }
        return count>=num;
    }
    public static void subPlayerItemAmount(Player player , ItemStack targetItem,int num)
    {
        int itemsToRemove = num;
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item != null && item.isSimilar(targetItem))
            {
                int preAmount = item.getAmount();
                int newAmount = Math.max(0, preAmount - itemsToRemove);
                itemsToRemove = Math.max(0, itemsToRemove - preAmount);
                item.setAmount(newAmount);
                if(itemsToRemove == 0)
                {
                    break;
                }
            }
        }
    }
    public static void subPlayerItemAmount(Player player , String targetItemName,int num)
    {
        int itemsToRemove = num;
        for(ItemStack item : player.getInventory().getContents())
        {
            if (item != null) {
                String DisplayName =  item.getItemMeta().getDisplayName();
                if(DisplayName!= null && DisplayName.equals(targetItemName)){
                    int preAmount = item.getAmount();
                    int newAmount = Math.max(0, preAmount - itemsToRemove);
                    itemsToRemove = Math.max(0, itemsToRemove - preAmount);
                    item.setAmount(newAmount);
                    if(itemsToRemove == 0)
                    {
                        break;
                    }
                }

            }
        }
    }
    public static void subInventoryItemAmount(Inventory inventory , String targetItemName,int num)
    {
        int itemsToRemove = num;
        for(ItemStack item : inventory.getContents())
        {
            if (item != null) {
                String DisplayName =  item.getItemMeta().getDisplayName();
                if(DisplayName!= null && DisplayName.equals(targetItemName)){
                    int preAmount = item.getAmount();
                    int newAmount = Math.max(0, preAmount - itemsToRemove);
                    itemsToRemove = Math.max(0, itemsToRemove - preAmount);
                    item.setAmount(newAmount);
                    if(itemsToRemove == 0)
                    {
                        break;
                    }
                }

            }
        }
    }
}