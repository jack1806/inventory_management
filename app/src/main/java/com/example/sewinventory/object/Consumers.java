package com.example.sewinventory.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Consumers {

    Map<Integer, Consumer> consumers;

    public Consumers(){
        consumers = new HashMap<>();
    }

    public Consumers(Map<Integer, Consumer> consumers){
        this.consumers = consumers;
    }

    public Map<Integer, Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(Map<Integer, Consumer> consumers) {
        this.consumers = consumers;
    }

    public Consumer addConsumer(String name, String location){
        Integer id = 0;
        for (Integer entry : this.consumers.keySet()){
            if (entry>id)
                id = entry;
        }
        id += 1;
        this.consumers.put(id, new Consumer(id, name, location));
        return get(id);
    }

    public void removeConsumer(Integer id){
        if (this.consumers.containsKey(id))
            consumers.remove(id);
    }

    public Consumer get(Integer id){
        if (this.consumers.containsKey(id))
            return this.consumers.get(id);
        return null;
    }

    public Consumer get(String name){
        for (Map.Entry<Integer, Consumer> entry : this.consumers.entrySet())
            if (entry.getValue().getName().equalsIgnoreCase(name))
                return get(entry.getKey());
        return null;
    }

}
