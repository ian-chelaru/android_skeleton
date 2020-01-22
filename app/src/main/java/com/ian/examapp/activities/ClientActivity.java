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
import com.ian.examapp.adapters.ItemListAdapter;
import com.ian.examapp.model.Item;
import com.ian.examapp.services.ItemService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientActivity extends AppCompatActivity
{
    private static final int CREATE_ITEM_REQUEST = 1;

    private ItemService itemService;
    private ItemListAdapter itemListAdapter;
    private ACProgressFlower progressIndicator;
    private AlertDialog retryDialog;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        RecyclerView itemsRecyclerView = findViewById(R.id.items_recycler_view);
        itemListAdapter = new ItemListAdapter(this);
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
        retryDialog = getRetryDialog();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        getServerItems();
    }

    private void getServerItems()
    {
        progressIndicator.show();

        itemService.getRobotsByType(type).enqueue(new Callback<List<Item>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    List<Item> items = response.body();
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
                retryDialog.show();
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

    private AlertDialog getRetryDialog()
    {
        AlertDialog.Builder retryDialog = new AlertDialog.Builder(this);
        return retryDialog.setMessage("Please check your internet connection")
                .setPositiveButton("Retry", ((dialog, which) -> {
                    dialog.cancel();
                    getServerItems();
                }))
                .create();
    }

    public void createItem(View view)
    {
        Intent intent = new Intent(this, CreateItemActivity.class);
        startActivityForResult(intent, CREATE_ITEM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_ITEM_REQUEST)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                assert data != null;
                Item newItem = (Item) data.getSerializableExtra("new_item");
                newItem.setType(type);
                insertServerItem(newItem);
            }
        }
    }

    private void insertServerItem(Item newItem)
    {
        progressIndicator.show();

        itemService.insertItem(newItem).enqueue(new Callback<Item>()
        {
            @Override
            public void onResponse(@NotNull Call<Item> call, @NotNull Response<Item> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    Item item = response.body();
                    assert item != null;
                    itemListAdapter.insertItem(item);
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
                displayToastMessage("Network error. Robot can not be added");
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        Item itemSelected = itemListAdapter.getItemByPosition(item.getGroupId());
        switch (item.getItemId())
        {
            case R.id.update_height:
//                updateAge(itemSelected);
                showHeightDialog(itemSelected);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void updateHeight(Item item)
    {
        progressIndicator.show();

        itemService.updateHeight(item).enqueue(new Callback<Item>()
        {
            @Override
            public void onResponse(@NotNull Call<Item> call, @NotNull Response<Item> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    Item upItem = response.body();
                    assert upItem != null;
                    item.setHeight(upItem.getHeight());
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

    private void showHeightDialog(Item item)
    {
        AlertDialog.Builder retryDialog = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        retryDialog.setView(input);

        retryDialog.setMessage("Give the height")
                .setPositiveButton("OK", ((dialog, which) -> {
                    dialog.cancel();
                    item.setHeight(Integer.valueOf(input.getText().toString()));
                    updateHeight(item);
                }))
                .create();

        retryDialog.show();
    }
}
