package com.parithi.patientlist.helpers;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;

public class PatientHelper {

    public static final String TAG = PatientHelper.class.getSimpleName();

    private static volatile PatientHelper patientHelper;
    private static final Object LOCK = new Object();

    // Singleton reference
    public static PatientHelper getInstance(){
        if(patientHelper == null){
            synchronized (LOCK){
                if(patientHelper == null){
                    patientHelper = new PatientHelper();
                }
            }
        }
        return patientHelper;
    }


    // Perform the network operation and returns the patients objects
    public static List<Patient> getFHIR() {
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

}
