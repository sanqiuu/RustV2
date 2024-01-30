package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.LifeBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LifeBlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        LifeBlock.demark(event);
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        LifeBlock.mark(event);
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event)
    {
        LifeBlock.interact(event);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        LifeBlock.interact(event);
    }
}
