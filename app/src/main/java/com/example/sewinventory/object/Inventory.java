package com.example.sewinventory.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

    String inventory_name;
    Integer inventory_id;
    String inventory_location;
    Map<Integer, Integer> products_data;
    ArrayList<Integer> sales_id;

    public Inventory(){
        products_data = new HashMap<>();
        sales_id = new ArrayList<>();
        inventory_name = "";
        inventory_id = 0;
        inventory_location = "";
    }

    public Inventory(Integer id, String name, String location){
        inventory_name = name;
        inventory_id = id;
        inventory_location = location;
        products_data  = new HashMap<>();
        sales_id = new ArrayList<>();
    }

    public Inventory(Integer id, String name, String location, Map<Integer, Integer> products_data, ArrayList<Integer> sales_id){
        inventory_name = name;
        inventory_id = id;
        inventory_location = location;
        this.products_data  = products_data;
        this.sales_id = sales_id;
    }

    public ArrayList<Integer> getSales_id() {
        return sales_id;
    }

    public void setSales_id(ArrayList<Integer> sales_id) {
        this.sales_id = sales_id;
    }

    public Integer getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(Integer inventory_id) {
        this.inventory_id = inventory_id;
    }

    public Map<Integer, Integer> getProducts_data() {
        return products_data;
    }

    public void setProducts_data(Map<Integer, Integer> products_data) {
        this.products_data = products_data;
    }

    public String getInventory_name() {
        return inventory_name;
    }

    public void setInventory_name(String inventory_name) {
        this.inventory_name = inventory_name;
    }

    public String getInventory_location() {
        return inventory_location;
    }

    public void setInventory_location(String inventory_location) {
        this.inventory_location = inventory_location;
    }

    public void addProduct(Integer product_id, Integer quantity){
        products_data.put(product_id, quantity);
    }

}
