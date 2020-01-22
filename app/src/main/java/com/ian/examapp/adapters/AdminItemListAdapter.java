package com.ian.examapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ian.examapp.R;
import com.ian.examapp.model.Item;
import com.ian.examapp.viewholders.AdminItemViewHolder;

import java.util.List;

public class AdminItemListAdapter extends RecyclerView.Adapter<AdminItemViewHolder>
{
    private Context context;
    private List<Item> items;

    public AdminItemListAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public AdminItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.admin_item, parent, false);
        return new AdminItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminItemViewHolder holder, int position)
    {
        holder.bindViewHolder(items.get(position));
    }

    @Override
    public int getItemCount()
    {
        if (items != null)
        {
            return items.size();
        }
        return 0;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    public Item getItemByPosition(int position)
    {
        return items.get(position);
    }

    public void deleteItem(Item item)
    {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void insertItem(Item item)
    {
        items.add(item);
        notifyDataSetChanged();
    }
}
