package com.example.sewinventory.object;

import java.util.HashMap;
import java.util.Map;

public class Products {

    Map<Integer, Product> products;

    public Products(){
        this.products = new HashMap<>();
    }

    public Products(Map<Integer, Product> products){
        this.products = products;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }

    public void removeProduct(Integer id){
        if (this.products.containsKey(id))
            products.remove(id);
    }
    public Product addProduct(String name){
        Integer id = 0;
        for (Integer entry : this.products.keySet()){
            if (entry>id)
                id = entry;
        }
        id += 1;
        products.put(id, new Product(id, name));
        return get(id);
    }

    public Product get(Integer id){
        if (this.products.containsKey(id))
            return this.products.get(id);
        return null;
    }

    public Product get(String name){
        for (Map.Entry<Integer, Product> entry : this.products.entrySet())
            if (entry.getValue().getProductName().equalsIgnoreCase(name))
                return get(entry.getKey());
        return null;
    }

}
