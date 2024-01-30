package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

class CraftingTableHolder implements InventoryHolder {
    private CraftingTable gui;
    private Integer subGui;
    public CraftingTableHolder(CraftingTable gui,Integer subGui){

        this.gui = gui;
        this.subGui = subGui;
    }
    public  Integer getSubgui()
    {
        return subGui;
    }
    public CraftingTable getGUI()
    {
        return  gui;
    }
    @Override
    public Inventory getInventory() {

        return gui.getInventory();
    }

}
public class CraftingTable {
    private Inventory inventory;
    Player player;
    public CraftingTable(Player player){
        this.player = player;
    }
    public  Inventory getInventory()
    {
        return inventory;
    }
    private void addLore(ItemStack itemStack, String name, int number){
        ItemMeta itemMeta = itemStack.getItemMeta();
        String lore = name+"X"+number;
        List<String> lore_list;
        if(itemMeta.hasLore()){
            lore_list = itemMeta.getLore();
            if(!lore_list.contains(lore)){
                lore_list.add(lore);
            }
        }else{
            lore_list = new ArrayList<>();
            lore_list.add(lore);
        }
        itemMeta.setLore(lore_list);
        itemStack.setItemMeta(itemMeta);
    }
    private ItemStack toItem(Material type, String name, int num)
    {
        ItemStack itemStack = new ItemStack(type,num);
        if(name!=null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
    public  boolean buyGoods(Player player,Inventory inv,int index)
    {
        ItemStack item = inv.getItem(index);
        boolean canBuy = true;
        if(item!=null ){
            ItemMeta itemMeta = item.getItemMeta();
            if(!itemMeta.hasLore()){
                player.getInventory().addItem(item.clone());
                return true;
            }
            List<String> lore_list = itemMeta.getLore();
            for(String lore:lore_list){
                String object_name = lore.split("X")[0];
                int num = Integer.parseInt(lore.split("X")[1]);
                if(!ItemUtil.isPlayerItemEnough(player,object_name,num)){
                    canBuy = false;
                    break;
                }
            }

            if(canBuy){
                for(String lore:lore_list){
                    String object_name = lore.split("X")[0];
                    int num = Integer.parseInt(lore.split("X")[1]);
                    ItemUtil.subPlayerItemAmount(player,object_name,num);

                }
                ItemStack itemStack = item.clone();
                ItemMeta meta = itemStack.getItemMeta();
                meta.setLore(null);
                itemStack.setItemMeta(meta);
                player.getInventory().addItem(itemStack);
                player.sendMessage("购买成功");
            }else {
                player.sendMessage("材料不足,无法购买");
            }

        }
        return canBuy;
    }

    public void OpenCraftingTable()
    {
        inventory = Bukkit.createInventory(new CraftingTableHolder(this,null),9,"制作台");
        ItemStack item1 = toItem(Material.IRON_AXE,"工具",1);
        ItemStack item2 = toItem(Material.STRING,"材料",1);
        ItemStack item3 = toItem(Material.ARROW,"子弹",1);
        ItemStack item4 = toItem(Material.IRON_SWORD,"武器",1);
        ItemStack item5 = toItem(Material.IRON_CHESTPLATE,"护甲",1);
        ItemStack item6 = toItem(Material.WOOD,"建筑方块",1);
        ItemStack item7 = toItem(Material.TNT,"爆炸物",1);
        ItemStack item8 = toItem(Material.BOAT,"载具",1);
        ItemStack item9 = toItem(Material.FISHING_ROD,"测试",1);
        inventory.setItem(0,item1);
        inventory.setItem(1,item2);
        inventory.setItem(2,item3);
        inventory.setItem(3,item4);
        inventory.setItem(4,item5);
        inventory.setItem(5,item6);
        inventory.setItem(6,item7);
        inventory.setItem(7,item8);
        inventory.setItem(8,item9);
        player.openInventory(inventory);
    }
    private void InitTNTInventory(Inventory inv,Player player){

        ItemStack item1 = toItem(Material.TNT,"C4",1);
        if(!Blueprint.hasBlueprint(player, item1)){
            item1  = toItem(Material.STAINED_GLASS_PANE,"C4",1);
        }
        addLore(item1,"火药",1);
        addLore(item1,"煤",3);
        inv.setItem(0,item1);
    }
    private void InitTestInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.PAPER,"蓝图-C4",1);
        ItemStack item2 = toItem(Material.IRON_ORE,"铁矿",64);
        ItemStack item3 = toItem(Material.GOLD_ORE,"金矿",64);
        ItemStack item4 = toItem(Material.DIAMOND_ORE,"钻石矿",64);
        ItemStack item5 = toItem(Material.COAL_ORE,"煤矿",64);
        ItemStack item6 = toItem(Material.REDSTONE_ORE,"红石矿",64);
        ItemStack item7 = toItem(Material.LOG,"木头",64);
        ItemStack item8 = toItem(Material.STONE,"石头",64);
        ItemStack item9 = toItem(Material.COBBLESTONE,"圆石",64);
        ItemStack item10 = toItem(Material.BREAD,"面包",64);
        ItemStack item11 = toItem(Material.GOLDEN_APPLE,"金苹果",1);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
        inv.setItem(4,item5);
        inv.setItem(5,item6);
        inv.setItem(6,item7);
        inv.setItem(7,item8);
        inv.setItem(8,item9);
        inv.setItem(9,item10);
        inv.setItem(10,item11);
    }
    private void InitRustBlockInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.WOOD,"木板",1);
        addLore(item1,"木头",3);
        ItemStack item2 = toItem(Material.SMOOTH_BRICK,"石砖",1);
        addLore(item2,"石头",3);
        ItemStack item3 = toItem(Material.TRAPPED_CHEST,"领地柜",1);
        addLore(item3,"木头",20);
        ItemStack item4 = toItem(Material.FURNACE,"熔炉",1);
        addLore(item4,"圆石",5);
        ItemStack item5 = toItem(Material.WOOD_DOOR,"木门",1);
        addLore(item5,"木头",3);
        ItemStack item6 = toItem(Material.BED,"床",1);
        addLore(item6,"羊毛",1);
        ItemStack item7 = toItem(Material.CHEST,"箱子",1);
        addLore(item7,"木头",1);
        ItemStack item8 = toItem(Material.ANVIL,"铁砧",1);
        addLore(item8,"石头",5);
        addLore(item8,"铁锭",3);
        ItemStack item9 = toItem(Material.LADDER,"梯子",1);
        addLore(item9,"木头",1);
        ItemStack item10 = toItem(Material.IRON_BLOCK,"铁砖",1);
        addLore(item10,"铁锭",9);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
        inv.setItem(4,item5);
        inv.setItem(5,item6);
        inv.setItem(6,item7);
        inv.setItem(7,item8);
        inv.setItem(8,item9);
        inv.setItem(9,item10);
    }
    private void InitToolInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.TORCH,"火把",1);
        addLore(item1,"木头",1);
        ItemStack item2 = toItem(Material.WOOD_AXE,"木斧",1);
        addLore(item2,"木头",3);
        ItemStack item3 = toItem(Material.WOOD_PICKAXE,"木镐",1);
        addLore(item3,"木头",3);
        ItemStack item4 = toItem(Material.FISHING_ROD,"钓鱼竿",1);
        addLore(item4,"木头",5);
        addLore(item4,"丝线",1);
        ItemStack item5 = toItem(Material.WOOD_HOE,"木锄头",1);
        addLore(item5,"木头",3);
        ItemStack item6 = toItem(Material.STONE_AXE,"石斧",1);
        addLore(item6,"石头",3);
        ItemStack item7 = toItem(Material.STONE_PICKAXE,"石镐",1);
        addLore(item7,"石头",3);
        ItemStack item8 = toItem(Material.STONE_HOE,"石锄头",1);
        addLore(item8,"石头",3);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
        inv.setItem(4,item5);
        inv.setItem(5,item6);
        inv.setItem(6,item7);
        inv.setItem(7,item8);



    }
    private void InitAmmoInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.ARROW,"箭矢",64);
        addLore(item1,"木头",1);
        addLore(item1,"羽毛",1);
        inv.setItem(0,item1);
    }
    private void InitWeaponInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.BOW,"弓箭",1);
        addLore(item1,"木头",5);
        ItemStack item2 = toItem(Material.WOOD_SWORD,"木剑",1);
        addLore(item2,"木头",5);
        ItemStack item3 = toItem(Material.STONE_SWORD,"石剑",1);
        addLore(item3,"石头",5);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
    }
    private void InitArmorInventory(Inventory inv,Player player){

        ItemStack item1 = toItem(Material.LEATHER_BOOTS,"皮制靴",1);
        addLore(item1,"皮革",1);
        ItemStack item2 = toItem(Material.LEATHER_CHESTPLATE,"皮制胸甲",1);
        addLore(item2,"皮革",1);
        ItemStack item3 = toItem(Material.LEATHER_HELMET,"皮制头盔",1);
        addLore(item3,"皮革",1);
        ItemStack item4 = toItem(Material.LEATHER_LEGGINGS,"皮制护腿",1);
        addLore(item4,"皮革",1);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
    }
    private void InitMaterialInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.BREAD,"面包",1);
        addLore(item1,"小麦",1);
        ItemStack item2 = toItem(Material.GOLDEN_APPLE,"金苹果",1);
        addLore(item2,"苹果",1);
        addLore(item2,"金锭",1);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
    }
    private void InitVehicleInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.BOAT,"船",1);
        addLore(item1,"木头",20);
        inv.setItem(0,item1);
    }
    public  void OperateCraftingTable(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        InventoryHolder inventoryHolder = inv.getHolder();
        if(!(inventoryHolder instanceof CraftingTableHolder)) {
            return;
        }
        int index = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        CraftingTableHolder craftingTableHolder = (CraftingTableHolder) inventoryHolder;
        event.setCancelled(true);
        if(craftingTableHolder.getSubgui() == null) {
            if (index <= 8 && index>=0) {
                Inventory sub_inv = null;
                switch (index) {
                    case 0:
                        //工具
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 0), 36, "工具");
                        InitToolInventory(sub_inv,player);
                        break;
                    case 1:
                        //消耗品
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 1), 36, "材料");
                        InitMaterialInventory(sub_inv,player);
                        break;
                    case 2:
                        //子弹
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 2), 36, "子弹");
                        InitAmmoInventory(sub_inv,player);
                        break;
                    case 3:
                        //武器
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 3), 36, "武器");
                        InitWeaponInventory(sub_inv,player);
                        break;
                    case 4:
                        //护甲
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 4), 36, "护甲");
                        InitArmorInventory(sub_inv,player);
                        break;

                    case 5:
                        //建造方块
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 5), 36, "建造方块");
                        InitRustBlockInventory(sub_inv,player);
                        break;
                    case 6:
                        //爆炸物
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 6), 36, "爆炸物");
                        InitTNTInventory(sub_inv,player);
                        break;
                    case 7:
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 6), 36, "载具");
                        InitVehicleInventory(sub_inv,player);
                        break;
                    case 8:
                        //测试
                        if(player.isOp()){
                            sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 7), 36, "测试");
                            InitTestInventory(sub_inv,player);
                        }
                        break;

                    default:
                        break;

                }
                if (sub_inv != null) {
                    player.openInventory(sub_inv);
                }
            }
        }else {
            if(index <= 35 && index>=0) {
                ItemStack item = inv.getItem(index);
                int sub_inv_index = craftingTableHolder.getSubgui();
                if(sub_inv_index == 8){
                    buyGoods(player,inv,index);
                }else{
                    if(item!=null){
                        if( item.getType() == Material.STAINED_GLASS_PANE
                                && !Blueprint.hasBlueprint(player, item)){
                            return;
                        }
                        buyGoods(player,inv,index);
                    }
                }


            }
        }

    }
}