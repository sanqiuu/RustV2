package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.TNT;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TNTListener implements Listener {
    @EventHandler
    public void onTNTPlace(BlockPlaceEvent event)
    {
        Block block = event.getBlock();
        TNT tnt = TNT.toTNT(block);
        if(tnt!=null){
            tnt.create();
        }

    }
    @EventHandler
    public void onTNTCancel(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        TNT tnt = TNT.toTNT(block);
        if(tnt!=null){
            tnt.cancel();
        }

    }
    @EventHandler
    public void onTNTDamageEntity(EntityDamageEvent event)
    {
        //Bukkit.getLogger().info(event.getCause().toString());
        if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return;
        if(event.getEntityType() == EntityType.DROPPED_ITEM) {
            event.setCancelled(true);
        }else if (event.getEntityType() == EntityType.PLAYER){
            event.setDamage(event.getDamage()* TNT.harm_player_percentage);
        }
    }
}