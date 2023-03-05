package com.example.timepassapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private List<String> categories;

    // Constructor to initialize list of categories
    public DrawerAdapter(List<String> categories) {
        this.categories = categories;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get category at current position
        String category = categories.get(position);

        // Set text of TextView in ViewHolder to category
        holder.textView.setText(category);

        // Set OnClickListener for ViewHolder to launch CategoryActivity with category as extra
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CategoryActivity.class);
            intent.putExtra("category", category);
            v.getContext().startActivity(intent);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.drawer_item_title);
        }
    }
}

