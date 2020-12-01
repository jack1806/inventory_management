package com.example.sewinventory.object;

import java.util.HashMap;
import java.util.Map;

public class Inventories {

    Map<Integer, Inventory> inventories;
    Map<Integer, String> deleted_inv;

    public Inventories(){
        inventories = new HashMap<>();
        deleted_inv = new HashMap<>();
    }

    public Inventories(Map<Integer, Inventory> inventories){
        this.inventories = inventories;
        this.deleted_inv = new HashMap<>();
    }

    public Map<Integer, String> getDeleted_inv() {
        return deleted_inv;
    }

    public void setDeleted_inv(Map<Integer, String> deleted_inv) {
        this.deleted_inv = deleted_inv;
    }

    public Map<Integer, Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(Map<Integer, Inventory> inventories) {
        this.inventories = inventories;
    }

    public Inventory addInventory(String name, String location){
        Integer id = 0;
        for (Integer entry : this.inventories.keySet()){
            if (entry>id)
                id = entry;
        }
        id += 1;
        this.inventories.put(id, new Inventory(id, name, location));
        return get(id);
    }

    public void removeInventory(Integer id){
        if (this.inventories.containsKey(id)) {
            String name = this.inventories.get(id).getInventory_name();
            this.inventories.remove(id);
            this.deleted_inv.put(id, name);
        }
    }

    public Inventory get(Integer id){
        if (this.inventories.containsKey(id))
            return this.inventories.get(id);
        if (this.deleted_inv.containsKey(id))
            return new Inventory(id, this.deleted_inv.get(id), "");
        return null;
    }

    public Inventory get(String name){
        for (Map.Entry<Integer, Inventory> entry : this.inventories.entrySet())
            if (entry.getValue().getInventory_name().equalsIgnoreCase(name))
                return get(entry.getKey());
        return null;
    }

}
