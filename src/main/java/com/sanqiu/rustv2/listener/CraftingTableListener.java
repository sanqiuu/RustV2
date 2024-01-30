package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.CraftingTable;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftingTableListener implements Listener {
    @EventHandler
    public void onClickWorkbench(PlayerInteractEvent event) {
        Player player =  event.getPlayer();
        if(event.getHand()== EquipmentSlot.HAND)
        {
            Action action = event.getAction();
            if(action == Action.RIGHT_CLICK_BLOCK) {
                Material material = event.getClickedBlock().getType();
                if(material == Material.WORKBENCH)
                {
                    event.setCancelled(true);
                    CraftingTable table = new CraftingTable(player);
                    table.OpenCraftingTable();
                }
            }
        }
    }
    @EventHandler
    public void onClickWorkbenchInventory(InventoryClickEvent event) {
        Entity entity =  event.getWhoClicked();
        if(entity instanceof Player){
            Player player = (Player) entity;
            CraftingTable table = new CraftingTable(player);
            table.OperateCraftingTable(event);
        }

    }

    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent event) {
        //Bukkit.getLogger().info(event.getInventory().getType().toString());
        if(event.getInventory().getType() == InventoryType.CRAFTING){
            CraftingInventory inventory  = event.getInventory();
            ItemStack item = inventory.getResult();
            if(item == null) return;
            if(item.getType()!= Material.WORKBENCH){
                inventory.setResult(new ItemStack(Material.AIR));
            }
        }
    }

}
