package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddItem extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private SQLiteDatabase db;
    private ImageButton imageButton;
    private Button addButton;
    private EditText itemName;
    private EditText price;
    private Spinner spinner;
    private Map<String,Integer> map;
    private Bitmap bitImage;
    private byte[] image;
    private boolean update;
    private int id;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        imageButton = (ImageButton)findViewById(R.id.image);
        addButton = (Button)findViewById(R.id.add);
        itemName = (EditText)findViewById(R.id.item_name);
        price = (EditText)findViewById(R.id.price_val);
        spinner = (Spinner)findViewById(R.id.group_type);

        Intent intent = getIntent();
        update = intent.getBooleanExtra("update", false);
        id = intent.getIntExtra("id", -1);
        try {
            //todo check the text box that is not empty and show dialog to user if is empty
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);

            List<String> list = new ArrayList<>();
            map = new HashMap<>();
            Cursor cursor = db.rawQuery("select * from tblGroups where name is not null",null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    list.add(name);
                    map.put(name, cursor.getInt(cursor.getColumnIndex("id")));
                    cursor.moveToNext();
                }
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);

            if( update ){
                addButton.setText("update");
                cursor = db.rawQuery("select * from tblProducts where id = " + id,null);
                cursor.moveToFirst();
                itemName.setText(cursor.getString(cursor.getColumnIndex("name")));
                price.setText(cursor.getString(cursor.getColumnIndex("price")));
                int tmp = cursor.getInt(cursor.getColumnIndex("gid"));
                spinner.setSelection(list.indexOf(getKey(map, tmp)));
                image = cursor.getBlob(cursor.getColumnIndex("image"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void addPhoto(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permission not granted, request it.
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                //permission already granted
                pickImageFromGallery();
            }
        }
        else {
            //system os is less then marshmallow
            pickImageFromGallery();
        }

    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to image view
            Uri imageUri = data.getData();
            try {
                bitImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                image = getBitmapAsByteArray(bitImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if(bitmap == null) return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void addButton(View view){
        try {
            db = this.openOrCreateDatabase("MenuDb",MODE_PRIVATE,null);
            int groupID = map.get(spinner.getSelectedItem().toString());
            if( String.valueOf(itemName.getText()).isEmpty()){
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Enter name of product.");
                dlgAlert.create().show();
                return;
            }
            if( String.valueOf(price.getText()).isEmpty() ){
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Enter price of product.");
                dlgAlert.create().show();
                return;
            }
            String name = itemName.getText().toString();
            Double itemPrice =   Double.valueOf(price.getText().toString());
            ContentValues cv = new ContentValues();
            cv.put("gid",groupID);
            cv.put("image",image);
            cv.put("name",name);
            cv.put("price",itemPrice);
            if( update )
                db.update("tblProducts",cv,"id=?", new String[]{String.valueOf(id)});
            else db.insert("tblProducts",null,cv);
            finish();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}