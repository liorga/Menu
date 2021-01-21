package com.example.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
    }

    public void editItem(View view){
        Intent switch_intent = new Intent(this, EditItem.class);
        startActivity(switch_intent);
    }

    public void addItem(View view){
        Intent switch_intent = new Intent(this, AddItem.class);
        startActivity(switch_intent);
    }


}