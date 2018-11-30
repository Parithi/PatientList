package com.parithi.patientlist.helpers;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;

public class PatientApiHelper {

    public static final String TAG = PatientApiHelper.class.getSimpleName();

    private static volatile PatientApiHelper patientApiHelper;
    private static final Object LOCK = new Object();

    // Singleton reference
    public static PatientApiHelper getInstance(){
        if(patientApiHelper == null){
            synchronized (LOCK){
                if(patientApiHelper == null){
                    patientApiHelper = new PatientApiHelper();
                }
            }
        }
        return patientApiHelper;
    }


    // Perform the network operation and returns the patients objects
    public static List<Patient> getFHIR() {
        IGenericClient client;
        FhirContext ctx;
        ctx = FhirContext.forDstu3();
        ctx.setRestfulClientFactory(new OkHttpRestfulClientFactory(ctx));
        client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");

        // .lastUpdated(new DateRangeParam("2011-01-01","2018-11-25")) - to get latest data
        // Not used since unable to get proper records with names
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

}
