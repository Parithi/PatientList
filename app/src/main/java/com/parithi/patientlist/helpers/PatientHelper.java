package com.parithi.patientlist.helpers;

import android.os.AsyncTask;

import com.parithi.patientlist.utils.Constants;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;

public class PatientHelper {

    public static final String TAG = PatientHelper.class.getSimpleName();
    private PatientHelperCallbackListener callbackListener;
    private static PatientHelper patientHelper;

    // Singleton reference
    public static PatientHelper getInstance(){
        if(patientHelper == null){
            patientHelper = new PatientHelper();
        }
        return patientHelper;
    }

    public void retreivePatientData() {
        // Execute the asynctask
        new PatientsTask().execute();
    }

    // Assign callback listener
    public void setCallbackListener(PatientHelperCallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public void clear() {
        callbackListener = null;
    }

    // Asynctask to perform the network operation in background thread and retrieve the FHIR data objects (patients)
    private class PatientsTask extends AsyncTask<Void,Void,List<Patient>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Patient> doInBackground(Void... voids) {
            try{
                // perform the network operation
                return getFHIR();
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Patient> patients) {
            super.onPostExecute(patients);
            if(patients!=null){
                List<com.parithi.patientlist.models.Patient> patientData = new ArrayList();

                // For each patient, create a Patient object for showing in the list
                for(Patient patient : patients){
                    patientData.add(new com.parithi.patientlist.models.Patient(patient.getName().get(0).getNameAsSingleString(),patient.getBirthDate(),patient.getGender().getDisplay()));
                }

                // Notify the activity with the new data
                if(callbackListener!=null){
                    callbackListener.notifyData(patientData);
                }
            } else {
                // Notify the activity and show error
                if(callbackListener!=null){
                    callbackListener.notifyData(null);
                    callbackListener.showError(Constants.UNABLE_TO_RETREIVE_DATA);
                }
            }
        }
    }

    // Perform the network operation and returns the patients objects
    public List<Patient> getFHIR() {
        IGenericClient client;
        FhirContext ctx;
        ctx = FhirContext.forDstu3();
        ctx.setRestfulClientFactory(new OkHttpRestfulClientFactory(ctx));
        client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
        Bundle bundle = client.search().forResource(Patient.class)
                .where(Patient.NAME.isMissing(false))
                .and(Patient.BIRTHDATE.isMissing(false))
                .and(Patient.GENDER.isMissing(false))
                .sort().ascending(Patient.NAME)
                .count(10)
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(ctx, bundle, Patient.class);
    }

    public interface PatientHelperCallbackListener {
        void notifyData(List<com.parithi.patientlist.models.Patient> patientData);
        void showError(int message);
        void clearList();
    }
}
