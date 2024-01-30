package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.RustV2;
import com.sanqiu.rustv2.data.RPlayer;
import com.sanqiu.rustv2.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class LifeBlock {

    private static void addBlock(Block block,Player player){
        String uuid = player.getUniqueId().toString();
        for(RPlayer rPlayer:PlayerManager.INSTANCE.player_list){
            if(rPlayer.uuid.equals(uuid)){
                rPlayer.addBlock(block);
                break;
            }
        }
    }
    private static void removeBlock(Block block){
        for(RPlayer rPlayer:PlayerManager.INSTANCE.player_list){
            if(rPlayer.isPlayerPlace(block)){
              rPlayer.removeBlock(block);
            }
        }
    }
    private static void removeBlock(Block block,Player player){
        String uuid = player.getUniqueId().toString();
        for(RPlayer rPlayer:PlayerManager.INSTANCE.player_list){
            if(rPlayer.uuid.equals(uuid)){
                rPlayer.removeBlock(block);
                break;
            }
        }
    }
    private static Player getWhoPlace(Block block){
        Player player = null;
        for(RPlayer rPlayer:PlayerManager.INSTANCE.player_list){
            if(rPlayer.isPlayerPlace(block)){
                player = Bukkit.getPlayer(UUID.fromString(rPlayer.uuid));
            }
        }
        return player;
    }

    public static void mark(BlockPlaceEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        player.sendMessage("放置类型："+block.getType().toString()+"["+block.getData()+"]");
        addBlock(block,player);
    }
    public static void interact(PlayerInteractEvent event){
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if(!(block.getState().getData() instanceof Door))  return;
        Player who = getWhoPlace(block);

        if(who!=null){
            Player player = event.getPlayer();
            if(!player.getUniqueId().equals(who.getUniqueId())){;
                event.setCancelled(true);
                player.sendMessage("操作失败，该装置由["+ player.getName()+"]放置");
            }
        }
    }

    public static void interact(BlockExplodeEvent event){
        event.setCancelled(true);
        for(Block block: event.blockList()){
            Material material = block.getType();
            boolean isDestory = false;
            switch (material){
                case WOOD:
                    isDestory = true;
                    break;
                case SMOOTH_BRICK:
                    int id = block.getData();
                    if( id == 2) isDestory = true;
                    else if(id == 3) block.setData((byte)0);
                    else if(id ==0) block.setData((byte)2);
                    break;
            }
            if(isDestory){
                block.setType(Material.AIR);
                removeBlock(block);
            }
        }
    }
    public static void demark(BlockBreakEvent event){

        Block block = event.getBlock();
        Player player = event.getPlayer();
        Player who = getWhoPlace(block);
        if(who!=null){
            if(!who.getUniqueId().equals(player.getUniqueId())){
                event.setCancelled(true);
                player.sendMessage("你不能破坏["+ player.getName()+"]放置的方块");
            }else {
                removeBlock(block,player);
            }
        }else{
           if(!Ore.isOre(block)){
               event.setCancelled(true);
           }
        }
    }
}