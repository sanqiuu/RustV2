package com.sanqiu.rustv2.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RChunk {
    public int x;
    public int z;
    public List<Location> oreList;
    public List<Location> supplyList;

    public RChunk(int x,int z,List<Location> oreList ,List<Location> supplyList){
        this.x = x;
        this.z = z;
        this.oreList = oreList;
        this.supplyList = supplyList;
    }
    public RChunk(int x,int z){
        this.x = x;
        this.z = z;
    }
    public  boolean isOreListInit(){
        return oreList!=null;
    }
    public  boolean isSupplyListInit(){
        return  supplyList!=null;
    }
    public  void initOreList(){
        oreList = new ArrayList<>();
    }
    public  void initSupplyList(){
        supplyList = new ArrayList<>();
    }
    public  void addOre(Location Ore){
        this.oreList.add(Ore);
    }
    public  boolean isOre(Location Ore){
        return oreList.contains(Ore);
    }
    public  void addSupplyB0x(Location supplyB0x){
        this.supplyList.add(supplyB0x);
    }
}
