package com.sanqiu.rustv2.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RPlayer {
    public String uuid;
    public String name;
    public List<String> blueprintList;
    public List<Location> blocksList;

    public RPlayer(String uuid, List<String> blueprintList , List<Location> blocksList){
            this.uuid = uuid;
            name = Bukkit.getPlayer(UUID.fromString(uuid)).getName();
            this.blueprintList = blueprintList;
            this.blocksList = blocksList;

    }
    public RPlayer(String uuid){
        this.uuid = uuid;
        name = Bukkit.getPlayer(UUID.fromString(uuid)).getName();
        this.blueprintList =  new ArrayList<>();
        this.blocksList =  new ArrayList<>();
    }
    public boolean isPlayerPlace(Block block){
        return blocksList.contains(block.getLocation());
    }
    public boolean hasBlueprint(ItemStack item){
        boolean is = false;
        String name  = item.getItemMeta().getDisplayName();
        if(name == null) return is;
        return blueprintList.contains(name);
    }
    public void addBlueprint(ItemStack item){
        String string  = item.getItemMeta().getDisplayName();
        if(string == null)return;
        blueprintList.add(string);

    }
    public void addBlock(Block block){
        blocksList.add(block.getLocation());

    }
    public void removeBlock(Block block){
        blocksList.remove(block.getLocation());

    }
}
