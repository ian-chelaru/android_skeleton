package com.ian.examapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ian.examapp.R;
import com.ian.examapp.adapters.TypeListAdapter;
import com.ian.examapp.services.TypeService;

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

public class TypeActivity extends AppCompatActivity
{
    private TypeService typeService;
    private TypeListAdapter typeListAdapter;
    private ACProgressFlower progressIndicator;
    private AlertDialog retryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        RecyclerView typesRecyclerView = findViewById(R.id.types_recycler_view);
        typeListAdapter = new TypeListAdapter(this);
        typesRecyclerView.setAdapter(typeListAdapter);
        typesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:2202/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        typeService = retrofit.create(TypeService.class);

        progressIndicator = getProgressIndicator();
        retryDialog = getRetryDialog();

        getServerTypes();
    }

    public void navigateToRobotsPage(View view)
    {
        String type = ((Button) view).getText().toString();
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    public void getServerTypes()
    {
        progressIndicator.show();

        typeService.getTypes().enqueue(new Callback<List<String>>()
        {
            @Override
            public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    List<String> types = response.body();
                    typeListAdapter.setTypes(types);
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
            public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t)
            {
                progressIndicator.dismiss();
                displayToastMessage("Network error. Types can not be loaded");
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
                    getServerTypes();
                }))
                .create();
    }
}
