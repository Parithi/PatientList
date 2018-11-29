package com.parithi.patientlist.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parithi.patientlist.R;
import com.parithi.patientlist.utils.Constants;
import com.parithi.patientlist.utils.Utils;
import com.parithi.patientlist.viewmodels.PatientEditViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientDetailActivity extends AppCompatActivity {

    // variables to store patient name, gender and birthdate
    String patientId;
    boolean isEditMode;

    @BindView(R.id.patient_name_textview) TextView patientNameTextView;
    @BindView(R.id.patient_birth_date_textview) TextView patientBirthDateTextView;
    @BindView(R.id.patient_gender_textview) TextView patientGenderTextView;

    private PatientEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.patient_detail_title);

        if(savedInstanceState!=null) {
            isEditMode = savedInstanceState.getBoolean(Constants.EDIT_MODE);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(PatientEditViewModel.class);
        viewModel.getPatientDetail().observe(this,patientEntity -> {
            if(patientEntity!=null && !isEditMode){
                patientGenderTextView.setText(patientEntity.getGender());
                patientBirthDateTextView.setText(Utils.getFormattedDate(patientEntity.getBirthDate()));
                patientNameTextView.setText(patientEntity.getName());
            }
        });

        // Get the data from the bundle
        if (getIntent() != null && getIntent().getExtras() != null) {
            patientId = getIntent().getStringExtra(Constants.PATIENT_ID);
            viewModel.fetchPatientDetail(patientId);
        } else {
            Toast.makeText(PatientDetailActivity.this,R.string.unable_to_refresh_data,Toast.LENGTH_LONG).show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDIT_MODE,isEditMode);
        super.onSaveInstanceState(outState);
    }
}
