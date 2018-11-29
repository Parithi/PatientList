package com.parithi.patientlist.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
        SORT_BY_NAME(0),
        SORT_BY_BIRTH_DATE(1),
        SORT_BY_GENDER(2);

        private final int value;
        SORT_METHODS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static volatile PatientRepository instance;
    private static final Object LOCK = new Object();
    private MutableLiveData<List<PatientEntity>> patientList = new MutableLiveData<>();
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

    public void getPatientList(SORT_METHODS sortBy,String query) {
        executor.execute(()->{
            String parameterizedQuery = "";
            if(query!=null) {
                parameterizedQuery = "%" + query + "%";
            }

            Log.d("TROUBLE","Getting data from db : " + sortBy.name() + " query:" + query);

            switch (sortBy){
                case SORT_BY_NAME:
                    patientList.postValue((query==null) ? patientDatabase.patientDAO().getAllPatientDataByName() : patientDatabase.patientDAO().getAllPatientDataByName(parameterizedQuery));
                    break;
                case SORT_BY_GENDER:
                    patientList.postValue((query==null) ? patientDatabase.patientDAO().getAllPatientDataByGender() : patientDatabase.patientDAO().getAllPatientDataByGender(parameterizedQuery));
                    break;
                case SORT_BY_BIRTH_DATE:
                    patientList.postValue((query==null) ? patientDatabase.patientDAO().getAllPatientDataByBirthDate() : patientDatabase.patientDAO().getAllPatientDataByBirthDate(parameterizedQuery));
                    break;
                default:
                    patientList.postValue((query==null) ? patientDatabase.patientDAO().getAllPatientDataByName() : patientDatabase.patientDAO().getAllPatientDataByName(parameterizedQuery));
            }

            if(patientList.getValue()!=null){
                Log.d("TROUBLE","records count:" + patientList.getValue().size());
            }
        });
    }

    private PatientRepository(Context context){
        patientDatabase = PatientDatabase.getInstance(context);
        getPatientList(SORT_METHODS.SORT_BY_NAME,null);
    }

    public LiveData<List<PatientEntity>> getPatientList() {
        return patientList;
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
