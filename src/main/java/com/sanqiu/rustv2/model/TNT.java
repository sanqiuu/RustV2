package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.RustV2;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class TNT {
    Block block;
    public static double harm_player_percentage = 1.0;
    private static final float harm_radius=2.5f;
    public  int power;
    TNT(Block block){
        this.block = block;
    }
    public boolean isTNT(){
        return block.getType()== Material.TNT;
    }

    public static TNT toTNT(Block block){
        TNT tnt = new TNT(block);
        if(tnt.isTNT())return tnt;
        return null;
    }

    private void setTaskId(int TaskId){
        if(!block.hasMetadata("TaskId")){
            block.setMetadata("TaskId",new FixedMetadataValue(RustV2.getPlugin(),TaskId));
        }
    }
    private int getTaskId(){
        int taskid = Integer.MIN_VALUE;
        if(block.hasMetadata("TaskId")){
            taskid = block.getMetadata("TaskId").get(0).asInt();
        }
        return taskid;
    }
    private void clearTaskId(){
        block.removeMetadata("TaskId", RustV2.getPlugin());
    }
    public  void create(){


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskTimer(RustV2.getPlugin(), new Runnable(){
            int delay = 5;
            final Block r_block = block;
            final Location r_location = r_block.getLocation();
            final World r_world = r_block.getWorld();
            @Override
            public void run() {
                if(delay == 0) {
                    r_block.setType(Material.AIR);
                    r_world.spawnParticle(Particle.EXPLOSION_HUGE,r_location,1);
                    r_block.getWorld().playSound(r_location, Sound.ENTITY_GENERIC_EXPLODE,harm_radius,0.533F);
                    r_world.createExplosion(r_location,harm_radius);
                    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                    int task_id = getTaskId();
                    if (task_id !=  Integer.MIN_VALUE) {
                        scheduler.cancelTask(task_id);
                        clearTaskId();
                    }
                }else {
                    delay --;
                    r_block.getWorld().playSound(r_location, Sound.UI_BUTTON_CLICK,harm_radius,0.533F);
                }
            }
        }, 0,20);

        int task_id = task.getTaskId();
        setTaskId(task_id);
    }
    public  void cancel(){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        int task_id = getTaskId();
        if (task_id !=  Integer.MIN_VALUE) {
            scheduler.cancelTask(task_id);
            clearTaskId();
        }
    }
}