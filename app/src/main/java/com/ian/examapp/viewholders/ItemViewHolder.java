package com.ian.examapp.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.examapp.R;
import com.ian.examapp.model.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder
{
    private final TextView nameTextView;
    private final TextView quantityTextView;
    private final TextView priceTextView;

    public ItemViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.name_text_view);
        this.quantityTextView = itemView.findViewById(R.id.quantity_text_view);
        this.priceTextView = itemView.findViewById(R.id.price_text_view);
    }

    public void bindViewHolder(Item item)
    {
        nameTextView.setText(item.getName());
        quantityTextView.setText(String.valueOf(item.getQuantity()));
        priceTextView.setText(String.valueOf(item.getPrice()));
    }
}
