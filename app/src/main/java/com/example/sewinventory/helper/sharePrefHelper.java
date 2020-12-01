package com.example.sewinventory.helper;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.sewinventory.object.Activities;
import com.example.sewinventory.object.Activity;
import com.example.sewinventory.object.Consumer;
import com.example.sewinventory.object.Consumers;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;
import com.example.sewinventory.object.Product;
import com.example.sewinventory.object.Products;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sharePrefHelper {

    Gson gson = new Gson();
    public static final String TAG = "SharedPrefHelper";

    public sharePrefHelper(){}

    public boolean isLogin(SharedPreferences sharedPreferences){
        return sharedPreferences.getBoolean(sharePrefContract.SHARED_PREF_LOGIN, false);
    }

    public void setLogin(SharedPreferences preferences, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(sharePrefContract.SHARED_PREF_LOGIN, value);
        editor.apply();
    }

    //-------------------------------------------------CONSUMERS START----------------------------------------------

    public Consumers getConsumers(SharedPreferences preferences){
        Log.e(TAG, "getConsumers: "+preferences.getString(sharePrefContract.CONSUMERS, ""));
        Consumers consumers = gson.fromJson(preferences.getString(sharePrefContract.CONSUMERS, ""),
                Consumers.class);
        if(consumers==null)
            consumers = new Consumers();
        return consumers;
    }

    public boolean addConsumer(SharedPreferences preferences, String name, String location){
        Consumers consumers = getConsumers(preferences);
        if (consumers.get(name)!=null)
            return false;
        if (consumers.addConsumer(name, location)==null)
            return false;
        updateConsumers(preferences, consumers);
        return true;
    }

    public void deleteConsumer(SharedPreferences preferences, String name){
        Consumers consumers = getConsumers(preferences);
        if (consumers.get(name)==null)
            return;
        consumers.removeConsumer(consumers.get(name).getConsumer_id());
        updateConsumers(preferences, consumers);
    }

    public ArrayList<String> getConsumerNames(SharedPreferences preferences){
        ArrayList<String> result = new ArrayList<>();
        Consumers consumers = getConsumers(preferences);
        for (Consumer consumer : consumers.getConsumers().values())
            result.add(consumer.getName());
        return result;
    }

    public void updateConsumers(SharedPreferences preferences, Consumers consumers){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(sharePrefContract.CONSUMERS, gson.toJson(consumers));
        Log.e(TAG, "updateConsumers: "+gson.toJson(consumers));
        editor.apply();
    }

    //-------------------------------------------------CONSUMERS END------------------------------------------------

    //-------------------------------------------------INVENTORIES START--------------------------------------------

    public Inventories getInventories(SharedPreferences preferences){
        Log.e(TAG, "getInventories: "+preferences.getString(sharePrefContract.INVENTORIES, ""));
        Inventories inventories = gson.fromJson(preferences.getString(sharePrefContract.INVENTORIES, ""),
                Inventories.class);
        if(inventories==null)
            inventories = new Inventories();
        return inventories;
    }

    public boolean addInventory(SharedPreferences preferences, String name, String location){
        Inventories inventories = getInventories(preferences);
        if (inventories.get(name)!=null)
            return false;
        if (inventories.addInventory(name, location)==null)
            return false;
        updateInventories(preferences, inventories);
        return true;
    }

    public void deleteInventory(SharedPreferences preferences, String name){
        Inventories inventories = getInventories(preferences);
        if (inventories.get(name)==null)
            return;
        inventories.removeInventory(inventories.get(name).getInventory_id());
        updateInventories(preferences, inventories);
    }

    public ArrayList<String> getInventoryNames(SharedPreferences preferences){
        ArrayList<String> result = new ArrayList<>();
        Inventories inventories = getInventories(preferences);
        for (Inventory inventory : inventories.getInventories().values())
            result.add(inventory.getInventory_name());
        return result;
    }

    public void updateInventories(SharedPreferences preferences, Inventories inventories){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(sharePrefContract.INVENTORIES, gson.toJson(inventories));
        Log.e(TAG, "updateInventories: "+gson.toJson(inventories));
        editor.apply();
    }

    //-------------------------------------------------INVENTORIES END----------------------------------------------

    //-------------------------------------------------PRODUCTS START-----------------------------------------------

    public Products getProducts(SharedPreferences preferences){
        Log.e(TAG, "getProduct: "+preferences.getString(sharePrefContract.PRODUCTS, ""));
        Products products = gson.fromJson(preferences.getString(sharePrefContract.PRODUCTS, ""),
                Products.class);
        if(products==null)
            products = new Products();
        return products;
    }

    public boolean addProduct(SharedPreferences preferences, String name){
        Products products = getProducts(preferences);
        if (products.get(name)!=null)
            return false;
        if (products.addProduct(name)==null)
            return false;
        updateProducts(preferences, products);
        return true;
    }

    public void deleteProduct(SharedPreferences preferences, String name){
        Products products = getProducts(preferences);
        if (products.get(name)==null)
            return;
        products.removeProduct(products.get(name).getProduct_id());
    }

    public ArrayList<String> getProductNames(SharedPreferences preferences){
        ArrayList<String> result = new ArrayList<>();
        Products products = getProducts(preferences);
        for (Product product : products.getProducts().values())
            result.add(product.getProductName());
        return result;
    }

    public void updateProducts(SharedPreferences preferences, Products products){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(sharePrefContract.PRODUCTS, gson.toJson(products));
        Log.e(TAG, "updateProducts: "+gson.toJson(products));
        editor.apply();
    }

    //-------------------------------------------------PRODUCTS END------------------------------------------------

    //-------------------------------------------------ACTIVITIES START--------------------------------------------

    public Activities getActivities(SharedPreferences preferences){
        Log.e(TAG, "getActivities: "+preferences.getString(sharePrefContract.ACTIVITIES, ""));
        Activities activities = gson.fromJson(preferences.getString(sharePrefContract.ACTIVITIES, ""),
                Activities.class);
        if(activities==null)
            activities = new Activities();
        return activities;
    }

    public Activity addActivity(SharedPreferences preferences, String consumer_name){
        Activities activities = getActivities(preferences);
        Consumers consumers = getConsumers(preferences);
        Inventories inventories = getInventories(preferences);
        Activity activity = activities.addActivity(consumer_name);
        if (consumers.get(consumer_name)!=null) {
            consumers.get(consumer_name).getActivities().add(activity.getActivity_id());
            updateConsumers(preferences, consumers);
        }
        else {
            inventories.get(consumer_name).getSales_id().add(activity.getActivity_id());
            updateInventories(preferences, inventories);
        }
        updateActivities(preferences, activities);
        return activity;
    }

    public boolean addSaleInfo(SharedPreferences preferences, Integer activity_id, String inv_name,
                               String prod_name, Integer quantity){
        Activities activities = getActivities(preferences);
        Inventories inventories = getInventories(preferences);
        Products products = getProducts(preferences);
        Integer inv_id = inventories.get(inv_name).getInventory_id();
        Integer prod_id = products.get(prod_name).getProduct_id();
        if (inventories.get(inv_name).getProducts_data().get(prod_id)!=null) {
            Integer remaining_stock = inventories.get(inv_name).getProducts_data().get(prod_id) - quantity;
            if (remaining_stock<0)
                return false;
            inventories.get(inv_name).getProducts_data().remove(prod_id);
            inventories.get(inv_name).getProducts_data().put(prod_id, remaining_stock);
            if (activities.get(activity_id).getSales_info().containsKey(inv_id)) {
                Map<Integer, Integer> temp = activities.get(activity_id).getSales_info().get(inv_id);
                temp.put(prod_id, quantity);
                activities.get(activity_id).getSales_info().remove(inv_id);
                activities.get(activity_id).getSales_info().put(inv_id, temp);
            }
            else {
                Map<Integer, Integer> temp = new HashMap<>();
                temp.put(prod_id, quantity);
                activities.get(activity_id).getSales_info().put(inv_id, temp);
            }
            if (!inventories.get(inv_name).getSales_id().contains(activity_id))
                inventories.get(inv_name).getSales_id().add(activity_id);
            updateActivities(preferences, activities);
            updateInventories(preferences, inventories);
            return true;
        }
        return false;
    }

    public void updateActivities(SharedPreferences preferences, Activities activities){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(sharePrefContract.ACTIVITIES, gson.toJson(activities));
        Log.e(TAG, "updateActivities: "+gson.toJson(activities));
        editor.apply();
    }

    //-------------------------------------------------ACTIVITIES END-----------------------------------------------

    //-------------------------------------------------MIXED ACTIONS------------------------------------------------

    public boolean addProductToInventory(SharedPreferences preferences, String product_name, String inventory_name, Integer quantity){
        Products products = getProducts(preferences);
        Inventories inventories = getInventories(preferences);
        if (products.get(product_name)==null || inventories.get(inventory_name)==null || quantity<1)
            return false;
        Integer product_id = products.get(product_name).getProduct_id();
        if (inventories.get(inventory_name).getProducts_data().containsKey(product_id)){
            Integer updated = inventories.get(inventory_name).getProducts_data().get(product_id) + quantity;
            inventories.get(inventory_name).getProducts_data().remove(product_id);
            inventories.get(inventory_name).getProducts_data().put(product_id, updated);
        }else
            inventories.get(inventory_name).getProducts_data().put(product_id, quantity);
        updateInventories(preferences, inventories);
        return true;
    }

    public boolean updateProductQuantity(SharedPreferences preferences, String product_name, String inventory_name, Integer quantity){
        Products products = getProducts(preferences);
        Inventories inventories = getInventories(preferences);
        if (products.get(product_name)==null || inventories.get(inventory_name)==null || quantity<1)
            return false;
        Integer product_id = products.get(product_name).getProduct_id();
        inventories.get(inventory_name).getProducts_data().put(product_id, quantity);
        updateInventories(preferences, inventories);
        return true;
    }

}
