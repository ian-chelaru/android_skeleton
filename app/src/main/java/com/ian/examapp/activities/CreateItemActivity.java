package com.ian.examapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ian.examapp.R;
import com.ian.examapp.model.Item;

public class CreateItemActivity extends AppCompatActivity
{
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText quantityEditText;
    private EditText priceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        nameEditText = findViewById(R.id.name_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        quantityEditText = findViewById(R.id.quantity_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);
    }

    public void saveAction(View view)
    {
        String itemName = nameEditText.getText().toString();
        String itemDescription = descriptionEditText.getText().toString();
        Integer itemQuantity = Integer.valueOf(quantityEditText.getText().toString());
        Integer itemPrice = Integer.valueOf(priceEditText.getText().toString());

        Item item = new Item();
        item.setName(itemName);
        item.setDescription(itemDescription);
        item.setQuantity(itemQuantity);
        item.setPrice(itemPrice);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_item", item);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void cancelAction(View view)
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
