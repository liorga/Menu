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

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File storagePath = getApplication().getFilesDir();

        String myDbPath = storagePath + "/" + "MenuProject";

        try {
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
            db.execSQL("create table tblGroups (" +
                    "id integer PRIMARY KEY autoincrement," +
                    "name text," +
                    "picture image ); " );

            db.execSQL("create table tblProducts (" +
                    "id integer PRIMARY KEY autoincrement," +
                    "gid integer," +
                    "picture image," +
                    "name text," +
                    "price decimal(10,2)); " );

            db.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void openMenu(View view){
        Intent switch_intent = new Intent(this, Menu.class);
        startActivity(switch_intent);
    }

    public void editMenu(View view){

    }

}