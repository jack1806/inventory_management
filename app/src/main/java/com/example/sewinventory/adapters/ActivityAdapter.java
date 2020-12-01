package com.example.sewinventory.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sewinventory.R;
import com.example.sewinventory.object.Activities;
import com.example.sewinventory.object.Activity;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Product;
import com.example.sewinventory.helper.sharePrefHelper;
import com.example.sewinventory.object.Products;

import java.util.ArrayList;
import java.util.Map;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityView> {

    Activities activities_data;
    ArrayList<Activity> activities = new ArrayList<>();
    SharedPreferences sharedPreferences;
    sharePrefHelper prefHelper = new sharePrefHelper();

    public ActivityAdapter(SharedPreferences preferences){
        this.sharedPreferences = preferences;
        this.activities_data = prefHelper.getActivities(sharedPreferences);
        this.activities.addAll(this.activities_data.getActivityMap().values());
    }

    public ActivityAdapter(SharedPreferences preferences, Integer inventory_id){
        this.sharedPreferences = preferences;
        this.activities_data = prefHelper.getActivities(sharedPreferences);
        Inventories inventories = prefHelper.getInventories(sharedPreferences);
        for (Integer sales_id : inventories.get(inventory_id).getSales_id())
            this.activities.add(prefHelper.getActivities(sharedPreferences).get(sales_id));
    }

    public void updateData(Integer inventory_id){
        this.activities_data = prefHelper.getActivities(sharedPreferences);
        Inventories inventories = prefHelper.getInventories(sharedPreferences);
        this.activities = new ArrayList<>();
        for (Integer sales_id : inventories.get(inventory_id).getSales_id())
            this.activities.add(prefHelper.getActivities(sharedPreferences).get(sales_id));
    }

    @NonNull
    @Override
    public ActivityView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout_id = R.layout.activity_recycler_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout_id,parent,false);
        return new ActivityView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityView holder, int position) {
        holder.bind(this.activities.get(position), this.sharedPreferences);
    }

    @Override
    public int getItemCount() {
        return this.activities.size();
    }

    public class ActivityView extends RecyclerView.ViewHolder {

        TextView sale_info_product_name, sale_info_product_quantity, sale_info_inventory_name, consumer_name, sale_utc_time;
        Activity activity;
        sharePrefHelper prefHelper;

        public ActivityView(@NonNull View itemView) {
            super(itemView);
            prefHelper = new sharePrefHelper();
            sale_info_product_name = itemView.findViewById(R.id.product_names);
            sale_info_product_quantity = itemView.findViewById(R.id.product_quantities);
            sale_info_inventory_name = itemView.findViewById(R.id.inventory_names);
            consumer_name = itemView.findViewById(R.id.consumer_name);
            sale_utc_time = itemView.findViewById(R.id.sale_utc_time);
        }

        public void bind(Activity activity, SharedPreferences preferences){
            this.activity = activity;
            Map<Integer, Map<Integer, Integer>> sale_info = this.activity.getSales_info();
            Products products = prefHelper.getProducts(preferences);
            Inventories inventories = prefHelper.getInventories(preferences);
            StringBuilder inventory_names = new StringBuilder();
            StringBuilder products_names = new StringBuilder();
            StringBuilder products_quantities = new StringBuilder();
            for (Map.Entry<Integer, Map<Integer, Integer>> entry : sale_info.entrySet()){
                for (Map.Entry<Integer, Integer> product_info : entry.getValue().entrySet()){
                    inventory_names.append(inventories.get(entry.getKey()).getInventory_name());
                    inventory_names.append("\n");
                    products_names.append(products.get(product_info.getKey()).getProductName());
                    products_names.append("\n");
                    products_quantities.append(product_info.getValue().toString());
                    products_quantities.append("\n");
                }
            }
            sale_info_inventory_name.setText(inventory_names);
            sale_info_product_quantity.setText(products_quantities);
            sale_info_product_name.setText(products_names);
            consumer_name.setText(activity.getConsumerName());
            sale_utc_time.setText(activity.getSale_utc());
        }
    }
}
