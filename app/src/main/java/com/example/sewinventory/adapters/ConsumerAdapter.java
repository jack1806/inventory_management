package com.example.sewinventory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sewinventory.R;
import com.example.sewinventory.object.Consumer;
import com.example.sewinventory.object.Consumers;
import com.example.sewinventory.object.Inventory;

import java.util.ArrayList;

public class ConsumerAdapter extends RecyclerView.Adapter<ConsumerAdapter.ConsumerView> {

    ArrayList<Consumer> consumers = new ArrayList<>();
    Consumers consumers_data;
    DeleteConsumerInterface deleteConsumerInterface;

    public ConsumerAdapter(Consumers consumers_data, DeleteConsumerInterface deleteConsumerInterface){
        this.consumers_data = consumers_data;
        this.consumers.addAll(this.consumers_data.getConsumers().values());
        this.deleteConsumerInterface = deleteConsumerInterface;
    }

    @NonNull
    @Override
    public ConsumerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout_id = R.layout.consumer_recycler_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout_id,parent,false);
        return new ConsumerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumerView holder, int position) {
        holder.bind(this.consumers.get(position));
        holder.setDeleteConsumerInterface(deleteConsumerInterface);
    }

    @Override
    public int getItemCount() {
        return this.consumers.size();
    }

    public class ConsumerView extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView consumer_name, consumer_location;
        ImageView delete_consumer;
        DeleteConsumerInterface deleteConsumerInterface;
        Consumer consumer;

        public ConsumerView(@NonNull View itemView) {
            super(itemView);
            consumer_name = itemView.findViewById(R.id.consumer_name);
            consumer_location = itemView.findViewById(R.id.consumer_location);
            delete_consumer = itemView.findViewById(R.id.delete_consumer);
        }

        public void bind(final Consumer consumer){
            this.consumer = consumer;
            consumer_name.setText(consumer.getName());
            consumer_location.setText(consumer.getLocation());
            delete_consumer.setOnClickListener(this);
        }

        public void setDeleteConsumerInterface(DeleteConsumerInterface deleteConsumerInterface){
            this.deleteConsumerInterface = deleteConsumerInterface;
        }

        @Override
        public void onClick(View view) {
            deleteConsumerInterface.deleteConsumer(consumer);
        }
    }
}

