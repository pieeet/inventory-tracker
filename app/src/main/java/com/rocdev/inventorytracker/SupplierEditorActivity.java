package com.rocdev.inventorytracker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rocdev.inventorytracker.data.InventoryContract;

public class SupplierEditorActivity extends BaseActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText telephoneEditText;
    private Button addEditButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_editor);
        setTitle("Add Supplier");
        initViews();
        initListeners();
    }

    private void initViews() {
        nameEditText = (EditText) findViewById(R.id.supplierNameEditText);
        emailEditText = (EditText) findViewById(R.id.supplierEmailEditText);
        telephoneEditText = (EditText) findViewById(R.id.supplierTelephoneEditText);
        addEditButton = (Button) findViewById(R.id.addEditSupplierButton);
    }

    private void initListeners() {
        addEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSupplier();
            }
        });
    }

    private void saveSupplier() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(telephone)) {
            Toast.makeText(this, "Enter email or telephone", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryContract.SupplierEntry.COLUMN_SUPPLIER_NAME, name);
        values.put(InventoryContract.SupplierEntry.COLUMN_SUPPLIER_EMAIL, email);
        values.put(InventoryContract.SupplierEntry.COLUMN_SUPPLIER_TELEPHONE, telephone);
        Uri uri = getContentResolver().insert(InventoryContract.SupplierEntry.CONTENT_URI, values);
        long result = ContentUris.parseId(uri);

        if (result == -1) {
            Toast.makeText(this, "Error with saving supplier", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Supplier saved", Toast.LENGTH_SHORT).show();
        }
        finish();

    }

}
