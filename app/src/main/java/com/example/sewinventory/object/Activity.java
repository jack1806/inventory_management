package com.example.sewinventory.object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity {


    Integer activity_id;
    Map<Integer, Map<Integer, Integer>> sales_info = new HashMap<>();
    String sale_utc, consumer_name;

    public Activity(Integer activity_id, String consumer_name){
        this.activity_id = activity_id;
        this.consumer_name = consumer_name;
        this.sale_utc = new Date().toString();
        String[] utc = this.sale_utc.split(" ");
        StringBuilder utc_time = new StringBuilder();
        utc_time.append(utc[2]).append(" ").append(utc[1]);
        utc_time.append(" ").append(utc[utc.length-1]).append(" | ");
        utc_time.append(utc[3]);
        this.sale_utc = utc_time.toString();
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public String getConsumerName() {
        return consumer_name;
    }

    public void setConsumerName(String consumer_name) {
        this.consumer_name = consumer_name;
    }

    public Map<Integer, Map<Integer, Integer>> getSales_info() {
        return sales_info;
    }

    public String getSale_utc() {
        return sale_utc;
    }

    public void setSale_utc(String sale_utc) {
        this.sale_utc = sale_utc;
    }

    public void setSales_info(Map<Integer, Map<Integer, Integer>> sales_info) {
        this.sales_info = sales_info;
    }

}
