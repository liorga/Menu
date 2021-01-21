package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    private SQLiteDatabase db;
    private List<Object> items;
    private ListView listView;
    private boolean product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();

        product = intent.getBooleanExtra("group", false);
        int gid = intent.getIntExtra("id", -1);
        db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
        Cursor cursor;

        if( product )
            cursor = db.rawQuery("select * from tblProducts where gid = " + gid, null);
        else
            cursor = db.rawQuery("select * from tblGroups", null);

        cursor.moveToFirst();

        items = new ArrayList<>();

        //items.add(new Item(2,1,null,"aaa",0).toString());
        while(!cursor.isAfterLast()){
            Item tmp;
            if( product ){
                tmp = new Item(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("gid")),
                        cursor.getBlob(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getDouble(cursor.getColumnIndex("price")));
            }
            else{
                tmp = new Item(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getBlob(cursor.getColumnIndex("image")),
                        cursor.getString(cursor.getColumnIndex("name")));
            }

            items.add(tmp);
            cursor.moveToNext();
        }

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new CustomAdapter(items,this));


    }


}