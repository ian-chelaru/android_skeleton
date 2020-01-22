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
    private final TextView specsTextView;
    private final TextView typeTextView;
    private final TextView ageTextView;

    public AdminItemViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.nameTextView = itemView.findViewById(R.id.name_text_view);
        this.specsTextView = itemView.findViewById(R.id.specs_text_view);
        this.typeTextView = itemView.findViewById(R.id.type_text_view);
        this.ageTextView = itemView.findViewById(R.id.age_text_view);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void bindViewHolder(Item item)
    {
        nameTextView.setText(item.getName());
        specsTextView.setText(item.getSpecs());
        typeTextView.setText(item.getType());
        ageTextView.setText(String.valueOf(item.getAge()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(this.getAdapterPosition(), R.id.update_age, Menu.NONE, "Update age");
    }
}
