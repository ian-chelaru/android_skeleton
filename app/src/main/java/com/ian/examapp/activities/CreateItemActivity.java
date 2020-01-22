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
    private EditText specsEditText;
    private EditText heightEditText;
    private EditText ageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        nameEditText = findViewById(R.id.name_edit_text);
        specsEditText = findViewById(R.id.specs_edit_text);
        heightEditText = findViewById(R.id.height_edit_text);
        ageEditText = findViewById(R.id.age_edit_text);
    }

    public void saveAction(View view)
    {
        String itemName = nameEditText.getText().toString();
        String itemSpecs = specsEditText.getText().toString();
        Integer itemHeight = Integer.valueOf(heightEditText.getText().toString());
        Integer itemAge = Integer.valueOf(ageEditText.getText().toString());

        Item item = new Item();
        item.setName(itemName);
        item.setSpecs(itemSpecs);
        item.setHeight(itemHeight);
        item.setAge(itemAge);

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
