package com.example.sewinventory.object;

import java.util.HashMap;
import java.util.Map;

public class Activities {

    Map<Integer, Activity> activityMap;

    public Activities(){
        this.activityMap = new HashMap<>();
    }

    public Activities(Map<Integer, Activity> activityMap){
        this.activityMap = activityMap;
    }

    public Map<Integer, Activity> getActivityMap() {
        return activityMap;
    }

    public void setActivityMap(Map<Integer, Activity> activityMap) {
        this.activityMap = activityMap;
    }

    public Activity addActivity(String consumer_name){
        Integer id = 0;
        for (Integer entry : this.activityMap.keySet()){
            if (entry>id)
                id = entry;
        }
        id += 1;
        this.activityMap.put(id, new Activity(id, consumer_name));
        return get(id);
    }

    public Activity get(Integer id){
        return this.activityMap.get(id);
    }

}
