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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sewinventory.adapters.ProductAdapter;
import com.example.sewinventory.helper.*;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;
import com.example.sewinventory.object.Product;
import com.example.sewinventory.object.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    SharedPreferences preferences;
    sharePrefHelper prefHelper = new sharePrefHelper();
    RecyclerView products_list;
    FloatingActionButton add_product_buttton;
    Inventories inventories;
    Gson gson = new Gson();

    public ProductsFragment() {
        // Required empty public constructor
    }

    public void reloadFragment(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        preferences = getContext().getSharedPreferences(sharePrefContract.SHARED_PREF_NAME,
                sharePrefContract.SHARED_PREF_MODE);
        inventories = prefHelper.getInventories(preferences);
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        products_list = view.findViewById(R.id.products_list);
        add_product_buttton = view.findViewById(R.id.add_product);

        Products products = prefHelper.getProducts(preferences);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        ProductAdapter productAdapter = new ProductAdapter(products, preferences);
        products_list.setLayoutManager(layoutManager);
        products_list.setAdapter(productAdapter);

        if (products.getProducts().size()==0)
            view.findViewById(R.id.no_product_error).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.no_product_error).setVisibility(View.INVISIBLE);

        add_product_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> items = prefHelper.getInventoryNames(preferences);
                if(items.size()==0){
                    Toast.makeText(getContext(), "Please add Inventory", Toast.LENGTH_LONG).show();
                    return;
                }
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View promptView = layoutInflater.inflate(R.layout.add_product_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(promptView);
                final EditText product_name_input;
                product_name_input = promptView.findViewById(R.id.product_name_input);

                builder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int index) {
                                        String prod_name;
                                        prod_name = product_name_input.getText().toString();
                                        if(prefHelper.addProduct(preferences, prod_name))
                                            reloadFragment();
                                        else
                                            Toast.makeText(getContext(), "Unable to add Product", Toast.LENGTH_SHORT).show();
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
