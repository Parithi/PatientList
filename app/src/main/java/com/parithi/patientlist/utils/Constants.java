package com.parithi.patientlist.utils;

import com.parithi.patientlist.R;

// Constant variables
public class Constants {

    public enum NETWORK_CODES {
        SUCCESS,
        FAILURE
    }

    // Intent Extra Labels
    public static final String PATIENT_ID = "PATIENT_ID";
    public static final String PATIENT_NAME = "PATIENT_NAME";
    public static final String PATIENT_GENDER = "PATIENT_GENDER";
    public static final String PATIENT_BIRTHDATE = "PATIENT_BIRTHDATE";

    // String references
    public static final int UNABLE_TO_RETREIVE_DATA = R.string.unable_to_refresh_data;
    public static final String EDIT_MODE = "EDIT_MODE";
}
