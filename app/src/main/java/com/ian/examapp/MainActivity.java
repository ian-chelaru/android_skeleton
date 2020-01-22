package com.ian.examapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ian.examapp.activities.AdminActivity;
import com.ian.examapp.activities.ClientActivity;
import com.ian.examapp.activities.TypeActivity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void navigateToClientSection(View view)
    {
        Intent intent = new Intent(this, TypeActivity.class);
        startActivity(intent);
    }

    public void navigateToAdminSection(View view)
    {
        if (isNetworkAvailable())
        {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
