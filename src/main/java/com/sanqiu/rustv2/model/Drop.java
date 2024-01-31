package com.sanqiu.rustv2.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Drop {
    public static int OreRate = 5;
    public static int AnimalDropRate = 5;
    public  static  ItemStack createCloth(int amount){
        ItemStack itemStack = new ItemStack(Material.PAPER,amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("布料");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
