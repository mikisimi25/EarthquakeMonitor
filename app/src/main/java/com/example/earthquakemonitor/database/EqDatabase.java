package com.example.earthquakemonitor.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.earthquakemonitor.Earthquake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//La version hay que cambiar cuando cambiamos algo en la entity
@Database(entities = {Earthquake.class}, version = 1)
public abstract class EqDatabase extends RoomDatabase {

    public abstract EqDao eqDao();

    private static volatile EqDatabase INSTANCE;

    //Creamos el hilo secundario, para crear, actualizar,... datos
    //Hasta 4 veces
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static EqDatabase getDatabase(final Context context) {
        if( INSTANCE == null ) {
            //synchronized -> si ya se estaba creando una bd, no se va a crear otra
            synchronized (EqDatabase.class) {
                if( INSTANCE == null ) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EqDatabase.class,"earthquakes_db").build();
                }
            }
        }

        return INSTANCE;
    }
}
