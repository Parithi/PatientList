package com.parithi.patientlist.models;

import java.util.Date;

// Patient Model
public class Patient {
    String name; // contains the first HumanName obtained from response
    Date birthDate; // contains the DOB of the patient
    String gender; // contains gender - male, female, unknown, other

    public Patient(String name, Date birthDate, String gender) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
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
