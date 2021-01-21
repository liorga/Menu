package com.example.menu;

import android.content.Context;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter implements ListAdapter {

    private List<Object> list = new ArrayList<>();
    private Context context;

    public CustomAdapter(List<Object> list, Context con) {
        this.list = list;
        context = con;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {

            //RelativeLayout inflater = (RelativeLayout) context.getSystemService(Context.Re);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //view =
            view = inflater.inflate(R.layout.item, null,false);
        }

        TextView tvContact= (TextView)view.findViewById(R.id.textView);
        tvContact.setText(((Item)list.get(position)).toString());
        ImageButton ib = (ImageButton)view.findViewById(R.id.image);


        if(((Item)list.get(position)).getGid() == -1 ){
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent switch_intent = new Intent(context,Menu.class);
                    switch_intent.putExtra("group", true);
                    switch_intent.putExtra("id", ((Item) list.get(position)).getId());
                    context.startActivity(switch_intent);
                }
            });
        }
        /**
         * image is null so its crushing with error in line 66
         */
        byte[] image = ((Item)list.get(position)).getImage();
        if (image == null){
            ib.setBackground(null);
            return view;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(),bitmap);
        ib.setBackground(bitmapDrawable);


        return view;
    }
}

