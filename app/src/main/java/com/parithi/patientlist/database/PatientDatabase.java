package com.parithi.patientlist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.parithi.patientlist.database.converters.DateTypeConverter;
import com.parithi.patientlist.database.patients.PatientDAO;
import com.parithi.patientlist.database.patients.PatientEntity;

@Database(entities = {PatientEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class PatientDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "patients.db";
    private static volatile PatientDatabase instance;
    private static final Object LOCK = new Object();

    public abstract PatientDAO patientDAO();

    public static PatientDatabase getInstance(Context context) {
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),PatientDatabase.class,DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
