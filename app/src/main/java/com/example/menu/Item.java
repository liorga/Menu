package com.example.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Item  {

    private int id,gid;
    private byte[] image;
    private String name;
    private double price;

    public Item(int id, int gid, byte[] image, String name, double price) {
        this.id = id;
        this.gid = gid;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public Item(int id, byte[] image, String name){
        this.id = id;
        this.image = image;
        this.name = name;
        gid = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        if( gid == -1 ) return name;
        return name + " | price: " + price;
    }
}
