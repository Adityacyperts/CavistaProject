package com.example.my_db;


import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = DatabaseHelper.NAME, version = DatabaseHelper.VERSION)
public class DatabaseHelper {
    public static final String NAME = "ImageGalleryDatabase";
    public static final int VERSION = 1;

}
