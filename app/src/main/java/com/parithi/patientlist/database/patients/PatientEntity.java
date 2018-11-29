package com.parithi.patientlist.database.patients;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

// PatientEntity Model
@Entity(tableName = "patients")
public class PatientEntity {

    @PrimaryKey @NonNull
    private String id;
    private String name; // contains the first HumanName obtained from response
    private Date birthDate; // contains the DOB of the patient
    private String gender; // contains gender - male, female, unknown, other

    public PatientEntity(String id, String name, Date birthDate, String gender) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    @Ignore
    public PatientEntity(String name, Date birthDate, String gender) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
