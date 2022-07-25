package com.example.earthquakemonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.earthquakemonitor.Earthquake;

import java.util.List;

@Dao
public interface EqDao {

    //onConflict -> que tiene que hacer si ya tiene guardado un terremoto con un cierto id
    //Replace -> reemplaza por el terremoto nuevo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Earthquake> eqList); //Guardar una lista de terremotos

    @Query("SELECT * FROM earthquakes")
    LiveData<List<Earthquake>> getEarthquakes();
}
