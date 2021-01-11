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

        //File storagePath = getApplication().getFilesDir();

        //String myDbPath = storagePath + "/" + "MenuProject";

        try {
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
            db.execSQL("create table tblGroups (" +
                    "integer id PRIMARY KEY autoincrement," +
                    "text name not null UNIQUE," +
                    "image picture ); " );

            db.execSQL("create table tblProducts (" +
                    "integer id PRIMARY KEY autoincrement," +
                    "integer gid," +
                    "image picture ," +
                    "text name not null UNIQUE," +
                    "price decimal(10,2)); " );

        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            db.close();
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