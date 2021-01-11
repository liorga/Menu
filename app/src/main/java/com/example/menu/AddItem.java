package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddItem extends AppCompatActivity {

    SQLiteDatabase db;
    ImageButton imageButton;
    Button addButton;
    EditText editText;
    Spinner spinner;


    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        imageButton = (ImageButton)findViewById(R.id.image);
        addButton = (Button)findViewById(R.id.add);
        editText = (EditText)findViewById(R.id.item_name);
        spinner = (Spinner)findViewById(R.id.group_type);

        try {
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);

            List<String> list = new ArrayList<>();

            Cursor cursor = db.rawQuery("select * from tblGroups where name is not null",null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    list.add(name);
                    cursor.moveToNext();
                }
            }


            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }


    }


    public void addPhoto(View view){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void addButton(View view){
        try {
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
            String res = editText.getText().toString();
            ContentValues cv = new ContentValues();
            cv.put("name",res);
            cv.put("picture",0);
            db.insert("tblGroups",null,cv);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }

    }
}