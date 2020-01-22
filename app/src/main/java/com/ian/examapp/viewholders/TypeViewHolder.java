package com.ian.examapp.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.examapp.R;

public class TypeViewHolder extends RecyclerView.ViewHolder
{
    private final Button typeButton;

    public TypeViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.typeButton = itemView.findViewById(R.id.type_button);
    }

    public void bindViewHolder(String type)
    {
        typeButton.setText(type);
    }
}
