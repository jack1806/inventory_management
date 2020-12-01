package com.example.sewinventory;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sewinventory.adapters.ConsumerAdapter;
import com.example.sewinventory.adapters.DeleteConsumerInterface;
import com.example.sewinventory.adapters.ProductAdapter;
import com.example.sewinventory.helper.sharePrefContract;
import com.example.sewinventory.helper.sharePrefHelper;
import com.example.sewinventory.object.Consumer;
import com.example.sewinventory.object.Consumers;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsumersFragment extends Fragment {

    SharedPreferences preferences;
    sharePrefHelper prefHelper = new sharePrefHelper();
    RecyclerView consumers_list;
    FloatingActionButton add_consumer_buttton;
    Consumers consumers;
    Gson gson = new Gson();

    public ConsumersFragment() {
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
        consumers = prefHelper.getConsumers(preferences);

        View view = inflater.inflate(R.layout.fragment_consumers, container, false);

        consumers_list = view.findViewById(R.id.customers_list);
        add_consumer_buttton = view.findViewById(R.id.add_consumer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        consumers_list.setLayoutManager(layoutManager);
        ConsumerAdapter consumerAdapter = new ConsumerAdapter(consumers,
                new DeleteConsumerInterface() {
                    @Override
                    public void deleteConsumer(final Consumer consumer) {
                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                        View confirmView = layoutInflater.inflate(R.layout.delete_confirmation_dialog_box, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setView(confirmView);
                        final EditText input_box = confirmView.findViewById(R.id.delete_response);

                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String confirm_message = input_box.getText().toString();
                                        if(confirm_message.equalsIgnoreCase("Confirm")) {
                                            prefHelper.deleteConsumer(preferences, consumer.getName());
                                            reloadFragment();
                                        }
                                        else
                                            Toast.makeText(getContext(), "Invalid text", Toast.LENGTH_SHORT).show();
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
        consumers_list.setAdapter(consumerAdapter);

        if (consumers.getConsumers().size()==0)
            view.findViewById(R.id.no_consumer_error).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.no_consumer_error).setVisibility(View.INVISIBLE);

        add_consumer_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Consumers consumers1 = prefHelper.getConsumers(preferences);
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View promptView = layoutInflater.inflate(R.layout.add_inventory_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(promptView);
                final EditText consumer_name_input, consumer_location_input;
                consumer_name_input = promptView.findViewById(R.id.inv_name_input);
                consumer_location_input = promptView.findViewById(R.id.inv_location_input);
                consumer_name_input.setHint("Consumer Name");
                consumer_location_input.setHint("Consumer Location");

                builder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int index) {
                                        String consumer_name, consumer_location;
                                        consumer_name = consumer_name_input.getText().toString();
                                        consumer_location = consumer_location_input.getText().toString();
                                        if(prefHelper.addConsumer(preferences, consumer_name, consumer_location))
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
