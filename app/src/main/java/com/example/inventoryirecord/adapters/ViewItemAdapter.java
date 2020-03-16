package com.example.inventoryirecord.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryirecord.R;
import com.example.inventoryirecord.data.InventoryItem;

import java.util.List;
import java.util.Objects;

public class ViewItemAdapter extends RecyclerView.Adapter<ViewItemAdapter.InventoryItemsViewHolder> {
    private List<InventoryItem> inventoryItems;
    private OnInventoryItemClickListener onInventoryItemClickListener;

    @NonNull
    @Override
    public InventoryItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.inventory_items, parent, false);
        return new InventoryItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemsViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(position % 2 == 0
                ? Color.rgb(224,214,158)
                : Color.rgb(240,230,174));
        holder.bind(inventoryItems.get(position));
    }

    @Override
    public int getItemCount() {
        return Objects.isNull(inventoryItems) ? 0 : inventoryItems.size();
    }

    public void updateInventoryItems(List<InventoryItem> items) {
        this.inventoryItems = items;
        notifyDataSetChanged();
    }

    public ViewItemAdapter(OnInventoryItemClickListener onInventoryItemClickListener) {
        this.onInventoryItemClickListener = onInventoryItemClickListener;
    }

    public interface OnInventoryItemClickListener {
        void onInventoryItemClicked(InventoryItem item);
    }

    class InventoryItemsViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView itemMakeTextView;
        private TextView itemTypeTextView;

        InventoryItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.inventory_items_name_text_view);
            itemMakeTextView = itemView.findViewById(R.id.inventory_items_make_text_view);
            itemTypeTextView = itemView.findViewById(R.id.inventory_items_type_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInventoryItemClickListener.onInventoryItemClicked(inventoryItems.get(getAdapterPosition()));
                }
            });
        }

        void bind(InventoryItem item) {
            itemNameTextView.setText(item.itemName);
            itemMakeTextView.setText(item.make);
            itemTypeTextView.setText(item.itemType);
        }
    }
}
