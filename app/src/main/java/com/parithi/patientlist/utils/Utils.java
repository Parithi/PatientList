package com.parithi.patientlist.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Utility classes
public class Utils {

    // Retrieve string in the format of MM-dd-YYYY for Date Objects
    public static String getFormattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy",Locale.CANADA);
        return format.format(date);
    }
}
