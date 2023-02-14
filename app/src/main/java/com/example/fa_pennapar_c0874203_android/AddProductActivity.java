package com.example.fa_pennapar_c0874203_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class AddProductActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_PRICE = "EXTRA_PRICE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_LAT = "EXTRA_LAT";
    public static final String EXTRA_LONG = "EXTRA_LONG";

    private EditText etProductName;
    private EditText etProductDescription;
    private EditText etProductPrice;
    private EditText etProductLatitude;
    private EditText etProductLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etProductName = findViewById(R.id.edit_text_name);
        etProductDescription = findViewById(R.id.edit_text_description);
        etProductPrice = findViewById(R.id.edit_text_price);
        etProductLatitude = findViewById(R.id.edit_text_lat);
        etProductLongitude = findViewById(R.id.edit_text_long);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Product");
            etProductName.setText(intent.getStringExtra(EXTRA_NAME));
            etProductDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            etProductPrice.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRICE, 0)));
            etProductLatitude.setText(String.valueOf(intent.getDoubleExtra(EXTRA_LAT, 0)));
            etProductLongitude.setText(String.valueOf(intent.getDoubleExtra(EXTRA_LONG, 0)));
        } else {
            setTitle("Add Product");
        }

        Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        if (etProductName.getText().toString().trim().isEmpty()
                || etProductDescription.toString().trim().isEmpty()
                || etProductPrice.getText().toString().isEmpty()
                || etProductLatitude.getText().toString().isEmpty()
                || etProductLongitude.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please insert name, price, description, lat and long", Toast.LENGTH_SHORT).show();
            return;
        }

        String productName = etProductName.getText().toString();
        String productDescription = etProductDescription.getText().toString();
        double productPrice = Double.parseDouble(etProductPrice.getText().toString());
        double productLatitude = Double.parseDouble(etProductLatitude.getText().toString());
        double productLongitude = Double.parseDouble(etProductLongitude.getText().toString());

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, productName);
        data.putExtra(EXTRA_PRICE, productPrice);
        data.putExtra(EXTRA_DESCRIPTION, productDescription);
        data.putExtra(EXTRA_LAT, productLatitude);
        data.putExtra(EXTRA_LONG, productLongitude);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
