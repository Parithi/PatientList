package com.parithi.patientlist;

import com.parithi.patientlist.helpers.PatientHelper;

import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {

    // Checks whether we get only 10 times from the response
    @Test
    public void checkDataCount() {
        assertEquals(10, PatientHelper.getInstance().getFHIR().size());
    }

    // Checks whether all the names are valid in the response
    @Test
    public void checkValidNames() {
        boolean hasValidNames = true;
        try{
            List<Patient> patientList = PatientHelper.getInstance().getFHIR();

            for(Patient patient : patientList){
                if(patient.getName().get(0).getNameAsSingleString()==null || patient.getName().get(0).getNameAsSingleString().trim().length() == 0){
                    hasValidNames = false;
                }
            }
        } catch (Exception e){
            hasValidNames = false;
        }

        assertTrue(hasValidNames);
    }

    // Checks whether all the dates are valid in the response
    @Test
    public void checkValidDates() {
        boolean hasValidDates = true;
        try{
            List<Patient> patientList = PatientHelper.getInstance().getFHIR();

            for(Patient patient : patientList){
                if(patient.getBirthDate() == null || !(patient.getBirthDate() instanceof Date)){
                    hasValidDates = false;
                }
            }
        } catch (Exception e){
            hasValidDates = false;
        }

        assertTrue(hasValidDates);
    }

    // Checks whether all the patients' gender is valid in the response
    @Test
    public void checkValidGenders() {
        boolean hasValidGenders = true;
        try{
            List<Patient> patientList = PatientHelper.getInstance().getFHIR();

            for(Patient patient : patientList){
                if(patient.getGender()==null || !patient.getGender().getDisplay().toLowerCase().matches("male|female|other|unknown")){
                    hasValidGenders = false;
                }
            }
        } catch (Exception e){
            hasValidGenders = false;
        }

        assertTrue(hasValidGenders);
    }
}