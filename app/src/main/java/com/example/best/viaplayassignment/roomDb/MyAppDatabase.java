package com.example.best.viaplayassignment.roomDb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;



@Database(entities = {Sections.class},version =1)
public abstract class MyAppDatabase extends RoomDatabase {
    
    public  abstract MyDao myDao();
}
