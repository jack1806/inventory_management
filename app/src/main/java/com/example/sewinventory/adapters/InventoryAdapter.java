package com.example.sewinventory.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sewinventory.InventoryDetailsActivity;
import com.example.sewinventory.R;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryView> {

    ArrayList<Inventory> inventories = new ArrayList<>();
    Inventories inventories_data;
    Context context;

    public InventoryAdapter(Context context, Inventories inventories_data){
        this.inventories_data = inventories_data;
        this.context = context;
        this.inventories.addAll(this.inventories_data.getInventories().values());
    }

    @NonNull
    @Override
    public InventoryView onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout_id = R.layout.inventory_recycler_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout_id,parent,false);
        return new InventoryView(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryView holder, int position) {
        holder.bind(this.inventories.get(position));
    }

    @Override
    public int getItemCount() {
        return this.inventories.size();
    }

    public class InventoryView extends RecyclerView.ViewHolder{

        TextView inv_name, inv_location, add_info;
        Inventory inventory;

        public InventoryView(@NonNull View itemView, final Context context) {
            super(itemView);
            inv_name = itemView.findViewById(R.id.inventory_name);
            inv_location = itemView.findViewById(R.id.inventory_location);
            add_info = itemView.findViewById(R.id.additional_info);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InventoryDetailsActivity.class);
                    intent.putExtra("inv_name", inv_name.getText().toString());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(final Inventory inventory){
            this.inventory = inventory;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.inventory.getProducts_data().size());
            stringBuilder.append(" products found in inventory");
            stringBuilder.append("\n");
            stringBuilder.append(this.inventory.getSales_id().size());
            stringBuilder.append(" activities include this inventory");
            add_info.setText(stringBuilder);
            inv_name.setText(inventory.getInventory_name());
            inv_location.setText(inventory.getInventory_location());
        }
    }
}

