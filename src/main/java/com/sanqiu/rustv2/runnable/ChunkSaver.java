package com.sanqiu.rustv2.runnable;

import com.sanqiu.rustv2.manager.ChunkManager;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkSaver extends BukkitRunnable {
    @Override
    public void run() {
        ChunkManager.INSTANCE.save();
    }
}
