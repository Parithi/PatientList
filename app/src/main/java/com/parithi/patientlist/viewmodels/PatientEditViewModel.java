package com.parithi.patientlist.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.parithi.patientlist.database.patients.PatientEntity;
import com.parithi.patientlist.repositories.PatientRepository;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PatientEditViewModel extends AndroidViewModel {

    private MutableLiveData<PatientEntity> patientEntityMutableLiveData = new MutableLiveData<>();
    private PatientRepository repository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public PatientEditViewModel(@NonNull Application application) {
        super(application);
        repository = PatientRepository.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<PatientEntity> getPatientDetail() {
        return patientEntityMutableLiveData;
    }

    public void fetchPatientDetail(String id) {
        executor.execute(() -> {
            patientEntityMutableLiveData.postValue(repository.getPatientDetail(id));
        });
    }

    public void savePatientData(String name, Date birthDate, String gender){
        PatientEntity patientEntity = patientEntityMutableLiveData.getValue();

        if(patientEntity!=null){
            patientEntity.setName(name.trim());
            patientEntity.setGender(gender.trim());
            patientEntity.setBirthDate(birthDate);
        }

        repository.savePatientDetail(patientEntity);
    }
}
