package com.ian.examapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.examapp.R;
import com.ian.examapp.viewholders.ItemViewHolder;
import com.ian.examapp.viewholders.TypeViewHolder;

import java.util.List;

public class TypeListAdapter extends RecyclerView.Adapter<TypeViewHolder>
{
    private Context context;
    private List<String> types;

    public TypeListAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.type, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position)
    {
        holder.bindViewHolder(types.get(position));
    }

    @Override
    public int getItemCount()
    {
        if (types != null)
        {
            return types.size();
        }
        return 0;
    }

    public void setTypes(List<String> types)
    {
        this.types = types;
        notifyDataSetChanged();
    }
}
