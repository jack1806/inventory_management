package com.example.sewinventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sewinventory.adapters.ActivityAdapter;
import com.example.sewinventory.helper.*;
import com.example.sewinventory.object.Activities;
import com.example.sewinventory.object.Activity;
import com.example.sewinventory.object.Consumer;
import com.example.sewinventory.object.Consumers;
import com.example.sewinventory.object.Inventories;
import com.example.sewinventory.object.Inventory;
import com.example.sewinventory.object.Product;
import com.example.sewinventory.object.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SharedPreferences preferences;
    sharePrefHelper prefHelper = new sharePrefHelper();
    FloatingActionButton add_sale_button;
    RecyclerView activites_recycler_view;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void reloadFragment(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        preferences = getContext().getSharedPreferences(sharePrefContract.SHARED_PREF_NAME, sharePrefContract.SHARED_PREF_MODE);

        add_sale_button = view.findViewById(R.id.add_sale);
        add_sale_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View add_sale_view = layoutInflater.inflate(R.layout.add_sale_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(add_sale_view);
                final Spinner C,I,P;
                final EditText quantity;
                Button add_sale_info = add_sale_view.findViewById(R.id.add_sale_info);
                final TextView sale_info = add_sale_view.findViewById(R.id.sale_info_text);
                quantity = add_sale_view.findViewById(R.id.product_quantity);

                C = add_sale_view.findViewById(R.id.consumers_spinner);
                I = add_sale_view.findViewById(R.id.inventories_spinner);
                P = add_sale_view.findViewById(R.id.products_spinner);

                final List<String> arr_P = prefHelper.getProductNames(preferences);
                final List<String> arr_C = prefHelper.getConsumerNames(preferences);
                final List<String> arr_I = prefHelper.getInventoryNames(preferences);

                if(arr_C.size()==0){
                    Toast.makeText(getContext(), "Please add Consumer", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(arr_I.size()==0){
                    Toast.makeText(getContext(), "Please add Inventory", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(arr_P.size()==0){
                    Toast.makeText(getContext(), "Please add Product", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayAdapter<String> adapter_C = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arr_C);
                ArrayAdapter<String> adapter_I = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arr_I);

                C.setAdapter(adapter_C);
                I.setAdapter(adapter_I);

                final Products products = prefHelper.getProducts(preferences);
                final Inventories inventories = prefHelper.getInventories(preferences);
                Inventory inventory = inventories.get(I.getSelectedItem().toString());

                List<String> arr_p = new ArrayList<>();
                for (Integer product_id : inventory.getProducts_data().keySet())
                    arr_p.add(products.get(product_id).getProductName());

                final ArrayAdapter<String> adapter_P = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, arr_p);
                P.setAdapter(adapter_P);

                I.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String inv_name = arr_I.get(i);
                        Inventory inventory1 = inventories.get(inv_name);
                        adapter_P.clear();
                        for (Integer product_id : inventory1.getProducts_data().keySet())
                            adapter_P.add(products.get(product_id).getProductName());
                        adapter_P.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                final Map<Integer, Map<Integer, Integer>> sale_summary = new HashMap<>();
                add_sale_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quant = quantity.getText().toString();
                        if (quant.isEmpty())
                            Toast.makeText(getContext(), "Invalid Quantity", Toast.LENGTH_SHORT).show();
                        else{
                            Integer sale_quantity = Integer.parseInt(quant);
                            Inventory inv = inventories.get(I.getSelectedItem().toString());
                            Product prod = products.get(P.getSelectedItem().toString());
                            if (inv.getProducts_data().get(prod.getProduct_id())<sale_quantity)
                                Toast.makeText(getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
                            else{
                                if (sale_summary.containsKey(inv.getInventory_id())) {
                                    if (sale_summary.get(inv.getInventory_id()).containsKey(prod.getProduct_id()))
                                        sale_summary.get(inv.getInventory_id()).remove(prod.getProduct_id());
                                    sale_summary.get(inv.getInventory_id()).put(prod.getProduct_id(), sale_quantity);
                                }
                                else {
                                    Map<Integer, Integer> temp = new HashMap<>();
                                    temp.put(prod.getProduct_id(), sale_quantity);
                                    sale_summary.put(inv.getInventory_id(), temp);
                                }
                                StringBuilder sale_summary_text = new StringBuilder();
                                sale_summary_text.append("Order Summary : \n");
                                for (Map.Entry<Integer, Map<Integer, Integer>> entry : sale_summary.entrySet()){
                                    for (Map.Entry<Integer, Integer> inv_entry : entry.getValue().entrySet()){
                                        sale_summary_text.append(inventories.get(entry.getKey()).getInventory_name());
                                        sale_summary_text.append("\t");
                                        sale_summary_text.append(products.get(inv_entry.getKey()).getProductName());
                                        sale_summary_text.append("\t");
                                        sale_summary_text.append(inv_entry.getValue().toString()).append("\n");
                                    }
                                }
                                sale_info.setText(sale_summary_text);
                            }
                        }
                    }
                });

                builder.setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Activity new_sale = prefHelper.addActivity(preferences, C.getSelectedItem().toString());
                                        if (new_sale!=null){
                                            Integer activity_id = new_sale.getActivity_id();
                                            Log.e("decrypt", sale_info.getText().toString());
                                            for (Map.Entry<Integer, Map<Integer, Integer>> entry : sale_summary.entrySet()){
                                                for (Map.Entry<Integer, Integer> inv_info : entry.getValue().entrySet()){
                                                    if (prefHelper.addSaleInfo(preferences, activity_id, inventories.get(entry.getKey()).getInventory_name(),
                                                            products.get(inv_info.getKey()).getProductName(), inv_info.getValue()))
                                                        continue;
                                                    Toast.makeText(getContext(), "Enable to add few items to sale", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            reloadFragment();
                                        }
                                        else
                                            Toast.makeText(getContext(), "Fail to add a sale", Toast.LENGTH_SHORT).show();
                                    }
                                });
                builder.create().show();
            }
        });

        if (prefHelper.getActivities(preferences).getActivityMap().size()==0)
            view.findViewById(R.id.no_activity_error).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.no_activity_error).setVisibility(View.INVISIBLE);

        activites_recycler_view = view.findViewById(R.id.activity_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        activites_recycler_view.setLayoutManager(layoutManager);
        ActivityAdapter activityAdapter = new ActivityAdapter(preferences);
        activites_recycler_view.setAdapter(activityAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
