package com.ian.examapp.viewholders;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.examapp.R;
import com.ian.examapp.model.Item;

public class AdminItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
{
    private final TextView nameTextView;
    private final TextView quantityTextView;
    private final TextView priceTextView;
    private final TextView statusTextView;

    public AdminItemViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.name_text_view);
        this.quantityTextView = itemView.findViewById(R.id.quantity_text_view);
        this.priceTextView = itemView.findViewById(R.id.price_text_view);
        this.statusTextView = itemView.findViewById(R.id.status_text_view);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void bindViewHolder(Item item)
    {
        nameTextView.setText(item.getName());
        quantityTextView.setText(String.valueOf(item.getQuantity()));
        priceTextView.setText(String.valueOf(item.getPrice()));
        statusTextView.setText(item.getStatus());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(this.getAdapterPosition(), R.id.delete_item, Menu.NONE, "Delete item");
    }
}
