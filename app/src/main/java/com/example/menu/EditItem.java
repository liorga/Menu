package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditItem extends AppCompatActivity {

    private Spinner groups;
    private Spinner products;
    private SQLiteDatabase db;
    private Map<String,Integer> groupsMap;
    private Map<String,Integer> productsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        groups = (Spinner)findViewById(R.id.groups);
        products = (Spinner)findViewById(R.id.products);

        try {
            //todo dialog if trying to edit empty pruduct

            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
            List<String> groupsList = new ArrayList<>();
            groupsMap = new HashMap<>();
            Cursor cursor = db.rawQuery("select * from tblGroups where name is not null",null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    groupsList.add(name);
                    groupsMap.put(name, cursor.getInt(cursor.getColumnIndex("id")));
                    cursor.moveToNext();
                }
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,groupsList);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            // attaching data adapter to spinner
            groups.setAdapter(dataAdapter);
            groups.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent, View view, int position, long id) {
                            changeProductsData();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                            changeProductsData();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void changeProductsData(){
        List<String> productsList = new ArrayList<>();
        productsMap = new HashMap<>();
        Cursor cursor = db.rawQuery("select * from tblProducts where gid = " + groupsMap.get(groups.getSelectedItem().toString()),null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                productsList.add(name);
                productsMap.put(name, cursor.getInt(cursor.getColumnIndex("id")));
                cursor.moveToNext();
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(EditItem.this,android.R.layout.simple_spinner_item,productsList);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // attaching data adapter to spinner
        products.setAdapter(dataAdapter1);
    }


    public void editClick(View view) {
        if( productsMap.size() == 0 ){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("No product selected.");
            dlgAlert.create().show();
            return;
        }
        Intent switch_intent = new Intent(this, AddItem.class);
        switch_intent.putExtra("update", true);
        switch_intent.putExtra("id", productsMap.get(products.getSelectedItem().toString()));
        startActivity(switch_intent);
        finish();
    }

    public void deleteClick(View view) {
        if( productsMap.size() == 0 ){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("No product selected.");
            dlgAlert.create().show();
            return;
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Delete this product");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.delete("tblProducts","id=?", new String[]{String.valueOf(productsMap.get(products.getSelectedItem().toString()))});
                        dialog.cancel();
                        finish();
                    }
                });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}