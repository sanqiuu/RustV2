package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.RustV2;
import com.sanqiu.rustv2.data.RChunk;
import com.sanqiu.rustv2.manager.ChunkManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

public class Radio {
    private  static final String teamID = "Radio";
    private  static final String metaID = "Radio";
    private static void  setRadioValue(Player player, int value){

        Scoreboard board = player.getScoreboard();
        Team team =  board.getTeam(teamID);
        if(team == null) return;
        player.setMetadata(metaID,new FixedMetadataValue(RustV2.getPlugin(),value));
        ChatColor color;
        if(value<=50){
            color = ChatColor.WHITE;
        }else if (value<=100){
            color = ChatColor.YELLOW;
        }else {
            color = ChatColor.RED;
        }
        team.setPrefix(ChatColor.AQUA+"辐射:"+color+value);
    }
    private static int  getRadioValue(Player player){
        if(!player.hasMetadata(metaID)){
            return 0;
        }
        return player.getMetadata(metaID).get(0).asInt();
    }
    private static void setRadioEffect(Player player, int radioValue){
        PotionEffect effect;
        //effect
        if(radioValue>=50){
            //player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,,2.0f,0.533F);
            effect = new PotionEffect(PotionEffectType.SLOW,
                    199980, 2, true, false);
            player.addPotionEffect(effect);
        }else {
            player.removePotionEffect(PotionEffectType.SLOW);
        }
        if(radioValue>=100){
            effect = new PotionEffect(PotionEffectType.CONFUSION,
                    199980, 2, true, false);
            player.addPotionEffect(effect);
        }
        else {
            player.removePotionEffect(PotionEffectType.CONFUSION);
        }
        if(radioValue>=120){
            player.damage(2);
        }

    }
    public static void  setRadio(Player player,int value){

        setRadioValue(player,value);
        setRadioEffect(player,value);
    }
    public static void ShowGUI(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(
                ChatColor.RED+"RustMC","SANQIU");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore(ChatColor.BLUE+"=-=-=-=-=-=-=-=-=-=");
        score.setScore(2);
        Team team = scoreboard.registerNewTeam(teamID);
        if(!player.hasMetadata(metaID)){
            player.setMetadata(metaID,new FixedMetadataValue(RustV2.getPlugin(),0));
        }
        String team_entry = ChatColor.BLACK + "" + ChatColor.WHITE;
        team.addEntry(team_entry);
        team.setPrefix(ChatColor.AQUA+"辐射:"+ChatColor.WHITE+player.getMetadata(metaID).get(0).asInt());

        objective.getScore(team_entry).setScore(1);

        Score score3 = objective.getScore(ChatColor.AQUA+"Made By "+ChatColor.WHITE+"Sanqiu");
        score3.setScore(0);
        player.setScoreboard(scoreboard);
    }

    public static void Radiate(Player player){
        Chunk chunk = player.getLocation().getChunk();
        double x = chunk.getX();
        double z = chunk.getZ();
        boolean isRadiate  = false;
        for (RChunk rChunk :ChunkManager.INSTANCE.chunk_list)
        {
            int c_x = rChunk.x;
            int c_z = rChunk.z;
            if(Math.abs(x-c_x)<=2 && Math.abs(z-c_z)<=2 && !rChunk.supplyList.isEmpty()){
                isRadiate = true;
                break;
            }
        }

        int value = getRadioValue(player);
        if(isRadiate){
            value+=9;

        }else{
            if(value>0){
                value--;
            }

        }

        setRadio(player,value);
    }
}
