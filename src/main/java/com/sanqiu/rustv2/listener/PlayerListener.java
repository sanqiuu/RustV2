package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.data.RPlayer;
import com.sanqiu.rustv2.manager.ChunkManager;
import com.sanqiu.rustv2.manager.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    boolean isFirstPlayer = true;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player=evt.getPlayer();
        player.sendMessage("Welcome to play RustMc, made by Sanqiu");
        if(isFirstPlayer){
            isFirstPlayer = false;
            PlayerManager.INSTANCE.load();
            ChunkManager.INSTANCE.load();
        }
        if(!PlayerManager.INSTANCE.contains(player)){
            PlayerManager.INSTANCE.player_list.add(new RPlayer(player.getUniqueId().toString()));
        }
    }
}
