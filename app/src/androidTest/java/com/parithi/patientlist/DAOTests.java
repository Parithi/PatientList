package com.parithi.patientlist;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.parithi.patientlist.database.PatientDatabase;
import com.parithi.patientlist.database.patients.PatientDAO;
import com.parithi.patientlist.database.patients.PatientEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DAOTests {

    public static final String LOG_TAG = DAOTests.class.getSimpleName();
    private PatientDatabase patientDatabase;
    private PatientDAO patientDAO;

    @Before
    public void createDB(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        patientDatabase = Room.inMemoryDatabaseBuilder(appContext,PatientDatabase.class).build();
        patientDAO = patientDatabase.patientDAO();
        Log.i(LOG_TAG, "createDB: Database Created");
    }

    @Test
    public void createPatient() {
        patientDAO.insertPatientData(new PatientEntity("1","ElamParithi Arul",new Date(),"male"));
        Log.i(LOG_TAG, "createPatient: Added " + patientDAO.getCount() + " patients");
        assertEquals(1,patientDAO.getCount());
    }

    @Test
    public void testDataClear() {
        patientDAO.deleteAllPatientData();
        Log.i(LOG_TAG, "testDataClear: Deleted All Data");
        assertEquals(0,patientDAO.getCount());
    }

    @After
    public void destroyDB(){
        patientDatabase.close();
        Log.i(LOG_TAG, "destroyDB: Database Destroyed");
    }

}
