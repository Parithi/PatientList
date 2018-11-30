package com.parithi.patientlist.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

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
    private MutableLiveData<Boolean> showLoading = new MutableLiveData<>();
    private LiveData<List<PatientEntity>> patientData;
    private Executor executor = Executors.newSingleThreadExecutor();

    // Intialize View Model
    public PatientListViewModel(@NonNull Application application) {
        super(application);
        repository = PatientRepository.getInstance(application.getApplicationContext());
        patientData = repository.getPatientList();
    }

    // Get Patient data from DB or Server
    // If no records are found in table, get data from Server and store it
    public void fetchPatientDataFromDb(PatientRepository.SORT_METHODS sortBy, String query) {
        executor.execute(()->{
            int patientListCount = repository.getCount();
            if(patientListCount == 0) {
                showLoading.postValue(true);
                fetchDataFromServer(sortBy);
            } else {
                showLoading.postValue(false);
                repository.getPatientList(sortBy,query);
            }
        });
    }

    // For showing Progress Bar
    public MutableLiveData<Boolean> getShowLoading() {
        return showLoading;
    }

    public LiveData<List<PatientEntity>> getPatientData() {
        return patientData;
    }

    public LiveData<Constants.NETWORK_CODES> getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Constants.NETWORK_CODES errorCode) {
        this.errorCode.postValue(errorCode);
    }

    // Tells PatientRepository to fetch data from server and save it to DB
    public void fetchDataFromServer(PatientRepository.SORT_METHODS sortBy){
        repository.fetchDataFromServer(new NetworkCallBack(){

            @Override
            public void onSuccess() {
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
