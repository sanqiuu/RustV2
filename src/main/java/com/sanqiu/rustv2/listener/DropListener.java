package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.Drop;
import com.sanqiu.rustv2.model.Ore;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DropListener implements Listener {
    @EventHandler
    public void onBlockDropItem(BlockBreakEvent event) {

        Block block = event.getBlock();

        if(Ore.isOre(block)){
            World world = block.getWorld();
            event.setDropItems(false);
            for(ItemStack itemStack:block.getDrops()){
                int amount = itemStack.getAmount();
                itemStack.setAmount(amount * Drop.OreRate);
                world.dropItemNaturally(event.getBlock().getLocation(), itemStack);
            }
            Ore.recover(block);
        }
    }
    @EventHandler
    public void onAnimalDeath(EntityDeathEvent event) {
        if(event.getEntityType() == EntityType.PLAYER) return;
        List<ItemStack> list = event.getDrops();
        for(ItemStack item : list){
            Material material = item.getType();
            switch (material){
                case ROTTEN_FLESH:
                case SPIDER_EYE:
                case BONE:
                    item.setType(Material.LEATHER);
                    break;
            }
            int amount = item.getAmount();
            item.setAmount(amount * Drop.AnimalDropRate);
        }
        ItemStack cloth = Drop.createCloth(10);
        list.add(cloth);
    }
}
