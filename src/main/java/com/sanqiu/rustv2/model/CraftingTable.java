package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.interfacee.GunInterface;
import com.sanqiu.rustv2.interfacee.VehicleInterface;
import com.sanqiu.rustv2.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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
enum MATERIAL_TYPE {
    WOOD("木头",Material.LOG,0),
    STONE("圆石",Material.COBBLESTONE,0),
    REDSTONE("红石",Material.REDSTONE,0),
    LEATHER("皮革",Material.LEATHER,0),
    COAL("煤",Material.COAL,0),
    IRON("铁",Material.IRON_INGOT,0),
    FEATHER("羽毛",Material.FEATHER,0),

    STRING("线",Material.STRING,0),
    WOOL("羊毛",Material.WOOL,0),
    FLINT("燧石",Material.FLINT,0),
    CLOTH("布料",Material.PAPER,1),
    TORCH("火把",Material.TORCH,0);


    private final String name;
    private final Material material;
    private final int value;

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    MATERIAL_TYPE(String name,Material material,int value) {
        this.value = value;
        this.name = name;
        this.material = material;
    }

    public int getValue() {
        return value;
    }
    public Material getType() {return material;}
    public String getName() {return name;}
    public static MATERIAL_TYPE fromName(String string) {
        MATERIAL_TYPE result =null;
        for (MATERIAL_TYPE value: MATERIAL_TYPE.values()){
            if(string.equals(value.name)){
                result =  value;
                break;
            }
        }
        assert  result!=null;
        return result;
    }

}
 class    MATERIAL {
     public static void addLore(ItemStack itemStack, MATERIAL_TYPE type, int number){
         ItemMeta itemMeta = itemStack.getItemMeta();
         String lore = type.getName() +"X"+number;
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
     public static List<ItemStack> fromLore(ItemStack itemStack){
         List<String> lore_list = itemStack.getItemMeta().getLore();
         List<ItemStack> list = new ArrayList<>();
         if(lore_list == null) return list;
         for(String lore:lore_list){
             String object_name = lore.split("X")[0];
             int num = Integer.parseInt(lore.split("X")[1]);
             MATERIAL_TYPE type = MATERIAL_TYPE.fromName(object_name);
             ItemStack item = new ItemStack(type.getType(),num);
             if(type.getValue() == 1){
                 ItemMeta meta = item.getItemMeta();
                 meta.setDisplayName(type.getName());
                 item.setItemMeta(meta);
             }
             list.add(item);
         }
         return  list;
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

    private ItemStack toItem(Material type, String name, int num,boolean needBlueprint)
    {
        ItemStack itemStack;
        ItemMeta itemMeta;
        itemStack  = new ItemStack(type,num);
        if(name!=null){
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
        }
        if(needBlueprint && !Blueprint.hasBlueprint(player, itemStack)){
            itemStack  = new ItemStack(Material.STAINED_GLASS_PANE);
            if(name!=null){
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(name);
                itemStack.setItemMeta(itemMeta);
            }
        }
        return itemStack;
    }
    private ItemStack toItem(ItemStack item,boolean needBlueprint)
    {
        ItemStack itemStack = item;
        if(needBlueprint && !Blueprint.hasBlueprint(player, itemStack)){
            itemStack  = new ItemStack(Material.STAINED_GLASS_PANE);
            ItemMeta itemMeta =  item.getItemMeta();
            String name = itemMeta.getDisplayName();
            if(name!=null){
                itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(name);
                itemStack.setItemMeta(itemMeta);
            }
        }
        return itemStack;
    }
    public  boolean buyGoods(Player player,Inventory inv,int index)
    {

        ItemStack item = inv.getItem(index);
        boolean canBuy = true;
        if(item!=null ){
            List<ItemStack> list = MATERIAL.fromLore(item);

            for(ItemStack is:list){
                int num = is.getAmount();
                if(!ItemUtil.isPlayerItemEnough(player,is,num)){
                    canBuy = false;
                    break;
                }
            }

            if(canBuy){
                for(ItemStack is:list){
                    int num = is.getAmount();
                    ItemUtil.subPlayerItemAmount(player,is,num);
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
        ItemStack item1 = toItem(Material.IRON_AXE,"工具",1,false);
        ItemStack item2 = toItem(Material.STRING,"材料",1,false);
        ItemStack item3 = toItem(Material.ARROW,"子弹",1,false);
        ItemStack item4 = toItem(Material.IRON_SWORD,"武器",1,false);
        ItemStack item5 = toItem(Material.IRON_CHESTPLATE,"护甲",1,false);
        ItemStack item6 = toItem(Material.WOOD,"建筑方块",1,false);
        ItemStack item7 = toItem(Material.TNT,"爆炸物",1,false);
        ItemStack item8 = toItem(Material.BOAT,"载具",1,false);
        ItemStack item9 = toItem(Material.FISHING_ROD,"测试",1,false);
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

        ItemStack item = toItem(Material.TNT,"TNT",1,true);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,64);
        MATERIAL.addLore(item,MATERIAL_TYPE.STRING,4);
        MATERIAL.addLore(item,MATERIAL_TYPE.COAL,10);
        inv.setItem(0,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.Grenade);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,64);
        MATERIAL.addLore(item,MATERIAL_TYPE.STRING,4);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,10);
        inv.setItem(1,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.GlitterBomb);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,50);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,50);
        MATERIAL.addLore(item,MATERIAL_TYPE.COAL,5);
        inv.setItem(2,item);
    }
    private void InitTestInventory(Inventory inv,Player player){
        ItemStack item1 = Drop.createBlueprint("TNT",1);
        ItemStack item2 = toItem(Material.IRON_ORE,null,64,false);
        ItemStack item3 = toItem(Material.GOLD_ORE,null,64,false);
        ItemStack item4 = toItem(Material.DIAMOND_ORE,null,64,false);
        ItemStack item5 = toItem(Material.COAL_ORE,null,64,false);
        ItemStack item6 = toItem(Material.REDSTONE_ORE,null,64,false);
        ItemStack item7 = toItem(Material.LOG,null,64,false);
        ItemStack item8 = toItem(Material.STONE,null,64,false);
        ItemStack item9 = toItem(Material.COBBLESTONE,null,64,false);
        ItemStack item10 = toItem(Material.BREAD,null,64,false);
        ItemStack item11 = toItem(Material.GOLDEN_APPLE,null,64,false);
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
        ItemStack item ;
         item = toItem(Material.WOOD,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,7);
        inv.setItem(0,item);
        item = toItem(Material.SMOOTH_BRICK,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,12);
        inv.setItem(1,item);
        item = toItem(Material.FURNACE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,25);
        inv.setItem(2,item);
        item = toItem(Material.WOOD_DOOR,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,240);
        inv.setItem(3,item);
        item = toItem(Material.BED,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,60);
        item = toItem(Material.CHEST,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,70);
        inv.setItem(4,item);
        item = toItem(Material.ANVIL,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,80);
        inv.setItem(5,item);
        item = toItem(Material.LADDER,null,1,false);
        MATERIAL. addLore(item,MATERIAL_TYPE.WOOD,5);
        inv.setItem(6,item);
        item = toItem(Material.TORCH,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.COAL,5);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,3);
        inv.setItem(7,item);
        item = toItem(Material.SIGN,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,30);
        inv.setItem(8,item);
        item = toItem(Material.TRAP_DOOR,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,50);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,25);
        inv.setItem(9,item);
        item = toItem(Material.WOOD_STAIRS,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,7);
        inv.setItem(10,item);
        item = toItem(Material.WOOL,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,5);
        inv.setItem(11,item);
        item = toItem(Material.IRON_DOOR,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        inv.setItem(12,item);
        item = toItem(Material.RAILS,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,5);
        inv.setItem(13,item);
        item = toItem(Material.IRON_FENCE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,20);
        inv.setItem(14,item);
        item = toItem(Material.SMOOTH_STAIRS,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,10);
        inv.setItem(15,item);

        item = toItem(Material.SMOOTH_BRICK,null,1,false);
        item.setDurability((short) 2);

        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(16,item);
        item = toItem(Material.SMOOTH_BRICK,null,1,false);
        item.setDurability((short) 3);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,15);
        inv.setItem(17,item);
        item = toItem(Material.REDSTONE_LAMP_OFF,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.TORCH,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,5);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,2);
        inv.setItem(18,item);
        item = toItem(Material.STAINED_GLASS,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,20);
        inv.setItem(19,item);
    }
    private void InitToolInventory(Inventory inv,Player player){

        ItemStack item = toItem(Material.STONE_AXE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(0,item);
        item = toItem(Material.STONE_PICKAXE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(1,item);
        item = toItem(Material.FISHING_ROD,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.STRING,44);
        inv.setItem(2,item);
        item = toItem(Material.STONE_HOE,null, 1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(3,item);
        item = toItem(Material.IRON_AXE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,30);
        inv.setItem(4,item);
        item = toItem(Material.IRON_PICKAXE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,50);
        inv.setItem(5,item);
        item = toItem(Material.IRON_HOE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,30);
        inv.setItem(6,item);

    }
    private void InitAmmoInventory(Inventory inv,Player player){
        ItemStack item = toItem(Material.ARROW,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,2);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,2);
        MATERIAL.addLore(item,MATERIAL_TYPE.FEATHER,2);
        inv.setItem(0,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.PistolAmmo);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,2);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,1);
        inv.setItem(1,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.ShotGunAmmo);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,5);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,3);
        inv.setItem(2,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.RifleAmmo);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,3);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,2);
        inv.setItem(3,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.RocketAmmo);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,45);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,10);
        inv.setItem(4,item);
    }
    private void InitWeaponInventory(Inventory inv,Player player){
        ItemStack item = toItem(Material.BOW,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,20);
        MATERIAL.addLore(item,MATERIAL_TYPE.STRING,10);
        inv.setItem(0,item);
        item = toItem(Material.STONE_SWORD,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,25);
        inv.setItem(1,item);
        item = toItem(Material.IRON_SWORD,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,50);
        inv.setItem(2,item);
        item = toItem(Material.SHIELD,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,50);
        inv.setItem(3,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.Pistol);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        inv.setItem(4,item);
        item = GunInterface.create(GunInterface.GUN_TYPE.ShotGun);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,20);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,5);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,50);
        inv.setItem(5,item);
        item = toItem(GunInterface.create(GunInterface.GUN_TYPE.Rifle),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,100);
        inv.setItem(6,item);
        item = toItem(GunInterface.create(GunInterface.GUN_TYPE.Sniper),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,100);
        inv.setItem(7,item);
        item = toItem(GunInterface.create(GunInterface.GUN_TYPE.Rocket),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,100);
        inv.setItem(8,item);
        item = toItem(GunInterface.create(GunInterface.GUN_TYPE.Submachine),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,100);
        inv.setItem(9,item);
    }
    private void InitArmorInventory(Inventory inv,Player player){

        ItemStack item = toItem(Material.LEATHER_BOOTS,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,8);
        inv.setItem(0,item);
        item = toItem(Material.LEATHER_CHESTPLATE,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,15);
        inv.setItem(1,item);
        item = toItem(Material.LEATHER_HELMET,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,5);
        inv.setItem(2,item);
        item = toItem(Material.LEATHER_LEGGINGS,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,10);
        inv.setItem(3,item);
        item = toItem(Material.IRON_BOOTS,"铁靴子",1,true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,16);
        inv.setItem(4,item);
        item = toItem(Material.IRON_CHESTPLATE,"铁胸甲",1,true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,30);
        inv.setItem(5,item);
        item = toItem(Material.IRON_HELMET,"铁头盔",1,true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,10);
        inv.setItem(6,item);
        item = toItem(Material.IRON_LEGGINGS,"铁护腿",1,true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,20);
        inv.setItem(7,item);
    }
    private void InitMaterialInventory(Inventory inv,Player player){
        ItemStack item = toItem(Material.STRING,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,1);
        inv.setItem(0,item);
        item = VehicleInterface.create(VehicleInterface.VEHICLE_TYPE.OilContent);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,2);
        inv.setItem(1,item);
    }
    private void InitVehicleInventory(Inventory inv,Player player){
        ItemStack item = toItem(Material.BOAT,null,1,false);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,500);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        inv.setItem(0,item);
        item = toItem(VehicleInterface.create(VehicleInterface.VEHICLE_TYPE.Plane),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,1000);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,1000);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,64);
        inv.setItem(1,item);
        item = toItem(VehicleInterface.create(VehicleInterface.VEHICLE_TYPE.Bike),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,300);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,32);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,32);
        inv.setItem(2,item);
        item = toItem(VehicleInterface.create(VehicleInterface.VEHICLE_TYPE.Car),true);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,500);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,64);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,64);
        inv.setItem(3,item);
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