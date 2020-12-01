package com.example.sewinventory.object;

import java.util.ArrayList;

public class Consumer {

    Integer consumer_id;
    String name,location;
    ArrayList<Integer> activities = new ArrayList<>();

    public Consumer(){}

    public Consumer(Integer id, String name, String location){
        this.consumer_id = id;
        this.name = name;
        this.location = location;
    }

    public Consumer(Integer id, String name, String location, ArrayList<Integer> activities){
        this.name = name;
        this.consumer_id = id;
        this.location = location;
        this.activities = activities;
    }

    public Integer getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(Integer consumer_id) {
        this.consumer_id = consumer_id;
    }

    public ArrayList<Integer> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Integer> activities) {
        this.activities = activities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
