package com.example.sewinventory;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sewinventory.adapters.DeleteInventoryInterface;
import com.example.sewinventory.adapters.InventoryAdapter;
import com.example.sewinventory.helper.*;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoriesFragment extends Fragment {


    FloatingActionButton floatingActionButton;
    sharePrefHelper prefHelper = new sharePrefHelper();
    Gson gson = new Gson();
    SharedPreferences preferences;

    public InventoriesFragment() {
        // Required empty public constructor
    }

    public void reloadFragment(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventories, container, false);

        preferences = getContext().getSharedPreferences(sharePrefContract.SHARED_PREF_NAME,
                sharePrefContract.SHARED_PREF_MODE);

        RecyclerView recyclerView = view.findViewById(R.id.inventories_list);
        floatingActionButton = view.findViewById(R.id.add_inventory);

        Inventories inventories = prefHelper.getInventories(preferences);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        InventoryAdapter inventoryAdapter = new InventoryAdapter(getContext(), inventories);
//        , new DeleteInventoryInterface() {
//            @Override
//            public void deleteInventory(final Inventory inventory) {
//                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//                View confirmView = layoutInflater.inflate(R.layout.delete_confirmation_dialog_box, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setView(confirmView);
//                final EditText input_box = confirmView.findViewById(R.id.delete_response);
//
//                builder.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                String confirm_message = input_box.getText().toString();
//                                if (confirm_message.equalsIgnoreCase("Confirm")) {
//                                    if (!(inventory.getProducts_data().size() != 0)) {
//                                        prefHelper.deleteInventory(preferences, inventory.getInventory_name());
//                                    } else {
//                                        Toast.makeText(getContext(), "Inventory not empty", Toast.LENGTH_SHORT).show();
//                                    }
//                                    reloadFragment();
//                                } else
//                                    Toast.makeText(getContext(), "Invalid text", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setCancelable(false)
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                builder.create().show();
//            }
//        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(inventoryAdapter);

        if (inventories.getInventories().size()==0)
            view.findViewById(R.id.no_inventory_error).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.no_inventory_error).setVisibility(View.INVISIBLE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Inventories inventories1 = prefHelper.getInventories(preferences);
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View promptView = layoutInflater.inflate(R.layout.add_inventory_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(promptView);
                final EditText inv_name_input, inv_location_input;
                inv_name_input = promptView.findViewById(R.id.inv_name_input);
                inv_location_input = promptView.findViewById(R.id.inv_location_input);

                builder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String inv_name, inv_location;
                                        inv_name = inv_name_input.getText().toString();
                                        inv_location = inv_location_input.getText().toString();
                                        if(inv_name.length()>0 && inv_location.length()>0) {
                                            if(prefHelper.addInventory(preferences, inv_name, inv_location))
                                                reloadFragment();
                                            else
                                                Toast.makeText(getContext(), "Inventory Name already exist", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(getContext(), "Empty String Inputs", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                builder.create().show();
            }
        });

        return view;
    }

}
