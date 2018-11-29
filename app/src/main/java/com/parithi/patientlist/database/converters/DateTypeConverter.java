package com.parithi.patientlist.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {

    @TypeConverter
    public static Date toLong(Long dateTimeStamp){
        return (dateTimeStamp == null) ? null : new Date(dateTimeStamp);
    }

    @TypeConverter
    public static Long toDate(Date date){
        return (date == null) ? null : date.getTime();
    }
}
