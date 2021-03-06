package com.parithi.patientlist.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// Utility classes
public class Utils {

    // Retrieve string in the format of MM-dd-YYYY for Date Objects
    public static String getFormattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy",Locale.CANADA);
        return format.format(date);
    }

    // Convert string (MM-dd-YYYY) to date
    public static Date getParsedDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy",Locale.CANADA);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Gender array for Spinner
    public static ArrayList<String> getGenderArray() {
        ArrayList<String> genderArray = new ArrayList<>();
        genderArray.add("");
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Unknown");
        genderArray.add("Other");
        return genderArray;
    }

    // Get Random Images
    public static String getImageUrl(int imageSize, String id) {
        return "https://picsum.photos/g/"+imageSize+"?random=" + id;
    }

}
