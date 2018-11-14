package com.example.best.viaplayassignment.roomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.ArrayList;
import java.util.List;

@Dao
public interface MyDao {

    @Insert
    long addToSections(Sections sections);


    @Query("select * from section")
    List<Sections> getSections();

    @Query("delete  from  section")
    public void clearSections();


    @Query("UPDATE section SET sec_offline=:jsonString WHERE sec_id =:secId")
    public  void updateSection(String jsonString,String secId);




}
