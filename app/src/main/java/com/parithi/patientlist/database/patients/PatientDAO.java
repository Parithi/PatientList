package com.parithi.patientlist.database.patients;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PatientDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPatientData(PatientEntity patientEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPatientData(List<PatientEntity> patientEntityList);

    @Query("SELECT * FROM patients WHERE id = :id")
    PatientEntity getPatientByID(String id);

    @Query("SELECT * FROM patients WHERE name LIKE :query ORDER BY name asc")
    List<PatientEntity> getAllPatientDataByName(String query);

    @Query("SELECT * FROM patients WHERE name LIKE :query ORDER BY gender asc")
    List<PatientEntity> getAllPatientDataByGender(String query);

    @Query("SELECT * FROM patients WHERE name LIKE :query ORDER BY birthDate desc")
    List<PatientEntity> getAllPatientDataByBirthDate(String query);

    @Query("SELECT * FROM patients ORDER BY name asc")
    List<PatientEntity> getAllPatientDataByName();

    @Query("SELECT * FROM patients ORDER BY gender asc")
    List<PatientEntity> getAllPatientDataByGender();

    @Query("SELECT * FROM patients ORDER BY birthDate desc")
    List<PatientEntity> getAllPatientDataByBirthDate();

    @Query("SELECT COUNT(*) FROM patients")
    int getCount();

    @Delete
    void deletePatientData(PatientEntity patientEntity);

    @Query("DELETE FROM patients")
    int deleteAllPatientData();

    @Delete
    public void deletePatientData(PatientEntity... patientEntities);
}
