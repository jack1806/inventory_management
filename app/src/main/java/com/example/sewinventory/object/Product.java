package com.example.sewinventory.object;

public class Product {

    String productName;
    Integer product_id;

    public Product(){
    }

    public Product(Integer id, String name){
        productName = name;
        product_id = id;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
