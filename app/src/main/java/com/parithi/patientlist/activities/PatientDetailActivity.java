package com.parithi.patientlist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parithi.patientlist.R;
import com.parithi.patientlist.utils.Constants;
import com.parithi.patientlist.utils.Utils;

import java.util.Date;

public class PatientDetailActivity extends AppCompatActivity {

    // variables to store patient name, gender and birthdate
    String patientName;
    Date patientBirthDate;
    String patientGender;

    TextView patientNameTextView;
    TextView patientBirthDateTextView;
    TextView patientGenderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.patient_detail_title);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Assign views to view references
        patientNameTextView = findViewById(R.id.patient_name_textview);
        patientBirthDateTextView = findViewById(R.id.patient_birth_date_textview);
        patientGenderTextView = findViewById(R.id.patient_gender_textview);

        // Get the data from the bundle
        if (getIntent() != null && getIntent().getExtras() != null) {
            patientName = getIntent().getStringExtra(Constants.PATIENT_NAME);
            patientGender = getIntent().getStringExtra(Constants.PATIENT_GENDER);
            patientBirthDate = new Date(getIntent().getLongExtra(Constants.PATIENT_GENDER, 0L));

            // Set the data
            patientGenderTextView.setText(patientGender);
            patientBirthDateTextView.setText(Utils.getFormattedDate(patientBirthDate));
            patientNameTextView.setText(patientName);
        } else {
            Toast.makeText(PatientDetailActivity.this,R.string.unable_to_retreive_data,Toast.LENGTH_LONG).show();
        }
    }

    // Finish the activity on pressing back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
