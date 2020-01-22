package com.ian.examapp.viewholders;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.examapp.R;
import com.ian.examapp.model.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
{
    private final TextView nameTextView;
    private final TextView heightTextView;

    public ItemViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.name_text_view);
        this.heightTextView = itemView.findViewById(R.id.height_text_view);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void bindViewHolder(Item item)
    {
        nameTextView.setText(item.getName());
        heightTextView.setText(String.valueOf(item.getHeight()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(this.getAdapterPosition(), R.id.update_height, Menu.NONE, "Update height");
    }
}
