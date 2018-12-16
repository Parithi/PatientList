package com.parithi.patientlist;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.github.javafaker.Faker;
import com.parithi.patientlist.helpers.PatientApiHelper;
import com.parithi.patientlist.repositories.PatientRepository;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parithi.patientlist.DAOTests.LOG_TAG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RepositoryUnitTests {

    private PatientRepository patientRepository;
    private PatientApiHelper apiHelper;

    // Mocking the PatientAPIHelper class to generate dummy patient data
    @Before
    public void initialize(){
        patientRepository = PatientRepository.getInstance(InstrumentationRegistry.getTargetContext());
        apiHelper = mock(PatientApiHelper.class);

        List<Patient> dummyPatients = new ArrayList<>();
        for(int i=0;i<10;i++){
            dummyPatients.add(getNewDummyPatient(String.valueOf(i)));
        }

        when(apiHelper.getFHIR()).thenReturn(dummyPatients);
    }

    // Generates a single dummy patient
    private Patient getNewDummyPatient(String id) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setBirthDate(new Date());
        patient.setGender((Math.random() < 0.5) ? Enumerations.AdministrativeGender.FEMALE : Enumerations.AdministrativeGender.MALE);
        HumanName name = new HumanName().setFamily(new Faker().name().fullName());
        List<HumanName> humanNames = new ArrayList<>();
        humanNames.add(name);
        patient.setName(humanNames);
        return patient;
    }

    // Checks whether all data is inserted to DB
    @Test
    public void checkForValidInsertion(){
        patientRepository.insertPatientDataToDB(apiHelper.getFHIR());
        assertEquals(apiHelper.getFHIR().size(),patientRepository.getCount());
    }

    // Clears the DB once the test is done
    @After
    public void destroyDB(){
        patientRepository.clearData();
        Log.i(LOG_TAG, "destroyDB: Database Destroyed");
    }
}