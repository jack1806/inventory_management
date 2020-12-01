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
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;
import com.example.sewinventory.object.Product;
import com.example.sewinventory.object.Products;
import com.example.sewinventory.helper.sharePrefHelper;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductView> {

    ArrayList<Product> products = new ArrayList<>();
    Products products_data;
    SharedPreferences sharedPreferences;

    public ProductAdapter(Products products_data, SharedPreferences preferences){
        this.products_data = products_data;
        this.products.addAll(this.products_data.getProducts().values());
        this.sharedPreferences = preferences;
    }

    @NonNull
    @Override
    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout_id = R.layout.product_recycler_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout_id,parent,false);
        return new ProductView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductView holder, int position) {
        holder.bind(this.products.get(position), sharedPreferences);
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class ProductView extends RecyclerView.ViewHolder {

        TextView product_name, inv_name, product_quant;
        Product product;
        sharePrefHelper prefHelper = new sharePrefHelper();

        public ProductView(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            inv_name = itemView.findViewById(R.id.inventory_names);
            product_quant = itemView.findViewById(R.id.product_quantities);
        }

        public void bind(Product product, SharedPreferences preferences){
            this.product = product;
            Inventories inventories = prefHelper.getInventories(preferences);
            Integer product_id = this.product.getProduct_id();
            StringBuilder inv_names = new StringBuilder();
            StringBuilder quantities = new StringBuilder();
            for (Inventory inventory : inventories.getInventories().values()){
                if (inventory.getProducts_data().containsKey(product_id)){
                    inv_names.append(inventory.getInventory_name());
                    inv_names.append("\n");
                    quantities.append(inventory.getProducts_data().get(product_id).toString());
                    quantities.append("\n");
                }
            }
            product_name.setText(product.getProductName());
            inv_name.setText(inv_names);
            product_quant.setText(quantities);
        }

    }
}
