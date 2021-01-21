package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
            db.execSQL("create table if not exists  tblGroups (" +
                    "id integer PRIMARY KEY autoincrement," +
                    "name VARCHAR UNIQUE not null," +
                    "image BLOB ) " );

            db.execSQL("create table if not exists tblProducts (" +
                    "id integer PRIMARY KEY autoincrement," +
                    "gid integer ," +
                    "image BLOB ," +
                    "name VARCHAR UNIQUE not null," +
                    "price decimal(10,2)) " );

        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void openMenu(View view){
        Intent switch_intent = new Intent(this, Menu.class);
        startActivity(switch_intent);
    }

    public void editMenu(View view){
        Intent switch_intent = new Intent(this, EditMenu.class);
        startActivity(switch_intent);
    }

}