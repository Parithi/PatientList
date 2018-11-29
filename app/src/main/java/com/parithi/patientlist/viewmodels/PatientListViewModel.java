package com.parithi.patientlist.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.parithi.patientlist.database.patients.PatientEntity;
import com.parithi.patientlist.repositories.PatientRepository;
import com.parithi.patientlist.utils.Constants;
import com.parithi.patientlist.utils.NetworkCallBack;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PatientListViewModel extends AndroidViewModel {

    private PatientRepository repository;
    private MutableLiveData<Constants.NETWORK_CODES> errorCode = new MutableLiveData<>();
    private MutableLiveData<List<PatientEntity>> patientData = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();
    private boolean isDataFetchedFromServer = false;

    public PatientListViewModel(@NonNull Application application) {
        super(application);
        repository = PatientRepository.getInstance(application.getApplicationContext());
    }

    public void fetchPatientDataFromDb(PatientRepository.SORT_METHODS sortBy, String query) {
        executor.execute(()->{
            int patientListCount = repository.getCount();
            if(patientListCount > 0){
                setPatientData(repository.getPatientList(sortBy,query));
            } else {
                fetchDataFromServer(sortBy);
            }
        });
    }

    public LiveData<List<PatientEntity>> getPatientData() {
        return patientData;
    }

    public void setPatientData(List<PatientEntity> patientData) {
        this.patientData.postValue(patientData);
    }

    public LiveData<Constants.NETWORK_CODES> getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Constants.NETWORK_CODES errorCode) {
        this.errorCode.postValue(errorCode);
    }

    public void fetchDataFromServer(PatientRepository.SORT_METHODS sortBy){
        repository.fetchDataFromServer(new NetworkCallBack(){

            @Override
            public void onSuccess() {
                isDataFetchedFromServer = true;
                fetchPatientDataFromDb(sortBy,null);
                setErrorCode(Constants.NETWORK_CODES.SUCCESS);
            }

            @Override
            public void onFailure() {
                setErrorCode(Constants.NETWORK_CODES.FAILURE);
            }
        });
    }

}
