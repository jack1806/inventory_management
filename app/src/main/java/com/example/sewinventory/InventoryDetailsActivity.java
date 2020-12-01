package com.example.sewinventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sewinventory.adapters.ActivityAdapter;
import com.example.sewinventory.helper.*;
import com.example.sewinventory.object.Activity;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;
import com.example.sewinventory.object.Products;

import java.security.Key;
import java.util.List;
import java.util.Map;

public class InventoryDetailsActivity extends AppCompatActivity implements View.OnKeyListener{

    TextView product_names, product_quantity;
    EditText edit_inv_name, edit_inv_location;
    Button update_products, add_products, delete_inventory;
    RecyclerView activities_view;
    String inv_name;
    SharedPreferences sharedPreferences;
    sharePrefHelper prefHelper = new sharePrefHelper();
    ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_details);
        inv_name = getIntent().getExtras().getString("inv_name");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(inv_name);

        sharedPreferences = getSharedPreferences(sharePrefContract.SHARED_PREF_NAME, sharePrefContract.SHARED_PREF_MODE);

        activities_view = findViewById(R.id.activity_list);
        product_names = findViewById(R.id.product_names);
        product_quantity = findViewById(R.id.product_quantities);
        add_products = findViewById(R.id.add_inv_product);
        update_products = findViewById(R.id.update_inv_product);
        delete_inventory = findViewById(R.id.delete_inventory);
        edit_inv_location = findViewById(R.id.edit_inventory_location);
        edit_inv_name = findViewById(R.id.edit_inventory_name);

        final Inventories inventories = prefHelper.getInventories(sharedPreferences);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InventoryDetailsActivity.this, RecyclerView.VERTICAL,false);
        activityAdapter = new ActivityAdapter(sharedPreferences, inventories.get(inv_name).getInventory_id());
        activities_view.setLayoutManager(layoutManager);
        activities_view.setAdapter(activityAdapter);

        delete_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(InventoryDetailsActivity.this);
                View confirmView = layoutInflater.inflate(R.layout.delete_confirmation_dialog_box, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(InventoryDetailsActivity.this);
                builder.setView(confirmView);
                final EditText input_box = confirmView.findViewById(R.id.delete_response);

                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String confirm_message = input_box.getText().toString();
                                if(confirm_message.equalsIgnoreCase("Confirm")) {
                                    prefHelper.deleteInventory(sharedPreferences, inv_name);
                                    finish();
                                }
                                else
                                    Toast.makeText(InventoryDetailsActivity.this, "Invalid text", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.create().show();
            }
        });

        update_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(InventoryDetailsActivity.this);
                View dialog = layoutInflater.inflate(R.layout.add_product_to_inventory_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(InventoryDetailsActivity.this);
                builder.setView(dialog);
                final Spinner product_spinner = dialog.findViewById(R.id.products_spinner);
                ArrayAdapter<String> product_spinner_adapter = new ArrayAdapter<String>(InventoryDetailsActivity.this,
                        R.layout.support_simple_spinner_dropdown_item, prefHelper.getProductNames(sharedPreferences));
                final EditText editText = dialog.findViewById(R.id.product_quantity);
                product_spinner.setAdapter(product_spinner_adapter);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String quantity = editText.getText().toString();
                                if (quantity.isEmpty()) {
                                    Toast.makeText(InventoryDetailsActivity.this, "Invalid quantity value", Toast.LENGTH_SHORT).show();
                                } else {
                                    String product_name = product_spinner.getSelectedItem().toString();
                                    Integer quant = Integer.parseInt(quantity);
                                    if (prefHelper.updateProductQuantity(sharedPreferences, product_name, inv_name, quant)) {
                                        reload();
                                        Toast.makeText(InventoryDetailsActivity.this, "Product updated to inventory", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(InventoryDetailsActivity.this, "Failed to add sale", Toast.LENGTH_SHORT).show();
                                }
                            }
                }).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        add_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(InventoryDetailsActivity.this);
                View dialog = layoutInflater.inflate(R.layout.add_product_to_inventory_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(InventoryDetailsActivity.this);
                builder.setView(dialog);
                final Spinner product_spinner = dialog.findViewById(R.id.products_spinner);
                ArrayAdapter<String> product_spinner_adapter = new ArrayAdapter<String>(InventoryDetailsActivity.this,
                        R.layout.support_simple_spinner_dropdown_item, prefHelper.getProductNames(sharedPreferences));
                final EditText editText = dialog.findViewById(R.id.product_quantity);
                product_spinner.setAdapter(product_spinner_adapter);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String quantity = editText.getText().toString();
                                if (quantity.isEmpty()) {
                                    Toast.makeText(InventoryDetailsActivity.this, "Invalid quantity value", Toast.LENGTH_SHORT).show();
                                } else {
                                    String product_name = product_spinner.getSelectedItem().toString();
                                    Integer quant = Integer.parseInt(quantity);
                                    if (prefHelper.addProductToInventory(sharedPreferences, product_name, inv_name, quant)) {
                                        reload();
                                        Toast.makeText(InventoryDetailsActivity.this, "Product added to inventory", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(InventoryDetailsActivity.this, "Failed to add sale", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setCancelable(false)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.create().show();
            }
        });

        edit_inv_name.setOnKeyListener(this);

        reload();

    }

    public void reload(){
        Inventories inventories = prefHelper.getInventories(sharedPreferences);
        Products products = prefHelper.getProducts(sharedPreferences);
        StringBuilder names = new StringBuilder();
        final StringBuilder quantities = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : inventories.get(inv_name).getProducts_data().entrySet()){
            names.append(products.get(entry.getKey()).getProductName());
            names.append("\n");
            quantities.append(entry.getValue().toString());
            quantities.append("\n");
        }
        if (inventories.get(inv_name).getProducts_data().size() == 0) {
            names.append("Null");
            quantities.append("Null");
        }
        product_names.setText(names);
        product_quantity.setText(quantities);

        edit_inv_name.setText(inv_name);
        edit_inv_location.setText(inventories.get(inv_name).getInventory_location());

        activityAdapter.updateData(inventories.get(inv_name).getInventory_id());
        activityAdapter.notifyDataSetChanged();

        if (inventories.get(inv_name).getSales_id().size()==0)
            findViewById(R.id.no_activity_error).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.no_activity_error).setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        EditText editText = (EditText) view;
        int view_id = view.getId();
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                i == KeyEvent.KEYCODE_ENTER){
            String new_input = editText.getText().toString();
            if (new_input.isEmpty()) {
                Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
                reload();
            }else{
                if (view_id == R.id.edit_inventory_name){
                    Inventories inventories = prefHelper.getInventories(sharedPreferences);
                    inventories.get(inv_name).setInventory_name(new_input);
                    prefHelper.updateInventories(sharedPreferences, inventories);
                    this.inv_name = new_input;
                }
                if (view_id == R.id.edit_inventory_location){
                    Inventories inventories = prefHelper.getInventories(sharedPreferences);
                    inventories.get(inv_name).setInventory_location(new_input);
                    prefHelper.updateInventories(sharedPreferences, inventories);
                }
                reload();
            }
        }
        return false;
    }
}