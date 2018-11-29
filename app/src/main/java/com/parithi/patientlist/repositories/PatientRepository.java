package com.parithi.patientlist.repositories;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.parithi.patientlist.database.PatientDatabase;
import com.parithi.patientlist.database.patients.PatientEntity;
import com.parithi.patientlist.helpers.PatientHelper;
import com.parithi.patientlist.utils.NetworkCallBack;

import org.hl7.fhir.dstu3.model.Patient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PatientRepository {

    public enum SORT_METHODS {
        SORT_BY_NAME,
        SORT_BY_BIRTH_DATE,
        SORT_BY_GENDER
    }

    private static volatile PatientRepository instance;
    private static final Object LOCK = new Object();
    private LiveData<List<PatientEntity>> patientList;
    private PatientDatabase patientDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static PatientRepository getInstance(Context context) {
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = new PatientRepository(context);
                }
            }
        }
        return instance;
    }

    public List<PatientEntity> getPatientList(SORT_METHODS sortBy,String query) {
        String parameterizedQuery = "";
        if(query!=null) {
            parameterizedQuery = "%" + query + "%";
        }
        switch (sortBy){
            case SORT_BY_NAME:
                return (query==null) ? patientDatabase.patientDAO().getAllPatientDataByName() : patientDatabase.patientDAO().getAllPatientDataByName(parameterizedQuery);
            case SORT_BY_GENDER:
                return (query==null) ? patientDatabase.patientDAO().getAllPatientDataByGender() : patientDatabase.patientDAO().getAllPatientDataByGender(parameterizedQuery);
            case SORT_BY_BIRTH_DATE:
                return (query==null) ? patientDatabase.patientDAO().getAllPatientDataByBirthDate() : patientDatabase.patientDAO().getAllPatientDataByBirthDate(parameterizedQuery);
            default:
                return (query==null) ? patientDatabase.patientDAO().getAllPatientDataByName() : patientDatabase.patientDAO().getAllPatientDataByName(parameterizedQuery);
        }
    }

    private PatientRepository(Context context){
        patientList = null;
        patientDatabase = PatientDatabase.getInstance(context);
    }


    public void fetchDataFromServer(NetworkCallBack networkCallBack){
        executor.execute(() -> {
            try{
                for (Patient patient : PatientHelper.getFHIR()) {
                    patientDatabase.patientDAO().insertPatientData(new PatientEntity(patient.getId(), patient.getName().get(0).getNameAsSingleString(), patient.getBirthDate(), patient.getGender().getDisplay()));
                }
                if(networkCallBack!=null){
                    networkCallBack.onSuccess();
                }
            } catch (Exception e){
                e.printStackTrace();
                if(networkCallBack!=null){
                    networkCallBack.onFailure();
                }
            }
        });
    }

    public PatientEntity getPatientDetail(String id) {
        return patientDatabase.patientDAO().getPatientByID(id);
    }

    public int getCount() {
        return patientDatabase.patientDAO().getCount();
    }

    public void savePatientDetail(final PatientEntity patientEntity){
        executor.execute(()->{
            patientDatabase.patientDAO().insertPatientData(patientEntity);
        });
    }

}
