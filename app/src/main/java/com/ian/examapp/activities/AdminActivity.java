package com.ian.examapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ian.examapp.R;
import com.ian.examapp.adapters.AdminItemListAdapter;
import com.ian.examapp.adapters.ItemListAdapter;
import com.ian.examapp.model.Item;
import com.ian.examapp.services.ItemService;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity
{
    private static final int CREATE_ITEM_REQUEST = 1;

    private ItemService itemService;
    private AdminItemListAdapter itemListAdapter;
    private ACProgressFlower progressIndicator;
    private AlertDialog ageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView itemsRecyclerView = findViewById(R.id.items_recycler_view);
        itemListAdapter = new AdminItemListAdapter(this);
        itemsRecyclerView.setAdapter(itemListAdapter);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // TODO change here the port
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:2202/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemService = retrofit.create(ItemService.class);

        progressIndicator = getProgressIndicator();

        getAllServerItems();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        Item itemSelected = itemListAdapter.getItemByPosition(item.getGroupId());
        switch (item.getItemId())
        {
            case R.id.update_age:
//                updateAge(itemSelected);
                showAgeDialog(itemSelected);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void updateAge(Item item)
    {
        progressIndicator.show();

        itemService.updateAge(item).enqueue(new Callback<Item>()
        {
            @Override
            public void onResponse(@NotNull Call<Item> call, @NotNull Response<Item> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    Item upItem = response.body();
                    assert upItem != null;
                    item.setAge(upItem.getAge());
                    itemListAdapter.notifyDataSetChanged();
                }
                else
                {
                    int errorStatusCode = response.code();
                    assert response.body() != null;
                    String errorMessage = response.body().toString();
                    displayToastMessage(errorStatusCode + " " + errorMessage);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Item> call, @NotNull Throwable t)
            {
                progressIndicator.dismiss();
                displayToastMessage("Network error. Robot can not be updates");
            }
        });
    }

    private void getAllServerItems()
    {
        progressIndicator.show();

        itemService.getAllItems().enqueue(new Callback<List<Item>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    List<Item> items = response.body();
                    assert items != null;
                    items = items.stream()
                            .sorted(Comparator.comparing(Item::getAge).reversed())
                            .limit(10)
                            .collect(Collectors.toList());
                    itemListAdapter.setItems(items);
                }
                else
                {
                    int errorStatusCode = response.code();
                    assert response.body() != null;
                    String errorMessage = response.body().toString();
                    displayToastMessage(errorStatusCode + " " + errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t)
            {
                progressIndicator.dismiss();
                displayToastMessage("Network error. Robots can not be loaded");
            }
        });

    }

    private void displayToastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private ACProgressFlower getProgressIndicator()
    {
        return new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();
    }

    private void showAgeDialog(Item item)
    {
        AlertDialog.Builder retryDialog = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        retryDialog.setView(input);

        retryDialog.setMessage("Give the age")
                .setPositiveButton("OK", ((dialog, which) -> {
                    dialog.cancel();
                    item.setAge(Integer.valueOf(input.getText().toString()));
                    updateAge(item);
                }))
                .create();

        retryDialog.show();
    }

}
