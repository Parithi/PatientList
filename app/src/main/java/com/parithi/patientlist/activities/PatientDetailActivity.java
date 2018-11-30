package com.parithi.patientlist.activities;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parithi.patientlist.R;
import com.parithi.patientlist.utils.Constants;
import com.parithi.patientlist.utils.Utils;
import com.parithi.patientlist.viewmodels.PatientEditViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientDetailActivity extends AppCompatActivity {

    // variables to store patientId
    String patientId;

    // Boolean to check whether the device is in EditMode
    boolean isEditMode;

    // Bind view instances to view resources
    @BindView(R.id.patient_name_textview) EditText patientNameEditTextView;
    @BindView(R.id.patient_birth_date_textview) TextView patientBirthDateEditTextView;
    @BindView(R.id.patient_gender_textview) Spinner patientGenderSpinner;
    @BindView(R.id.profile_imageview) ImageView patientImageView;

    // Array for displaying genders
    private ArrayList<String> genderArray = Utils.getGenderArray();

    // ViewModel to handle related functionalities of this view
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

        // Bind data to view resources
        ButterKnife.bind(this);

        // Initiate the spinner for showing genders
        patientGenderSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.gender_spinner_item, genderArray));

        // Initiate the view model and observe patient data changes
        viewModel = ViewModelProviders.of(this).get(PatientEditViewModel.class);
        viewModel.getPatientDetail().observe(this,patientEntity -> {
            if(patientEntity!=null && !isEditMode){
                patientNameEditTextView.setText(patientEntity.getName());
                patientBirthDateEditTextView.setText(Utils.getFormattedDate(patientEntity.getBirthDate()));
                patientGenderSpinner.setSelection(genderArray.indexOf(patientEntity.getGender().trim()));
                Glide.with(this).load(Utils.getImageUrl(128,patientEntity.getId())).into(patientImageView);

                patientBirthDateEditTextView.setOnClickListener(v -> {
                    Calendar mcurrentDate= new GregorianCalendar();
                    mcurrentDate.setTime(patientEntity.getBirthDate());
                    int currentYear=mcurrentDate.get(Calendar.YEAR);
                    int currentMonth=mcurrentDate.get(Calendar.MONTH);
                    int currentDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker=new DatePickerDialog(PatientDetailActivity.this, (view, year, month, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        patientEntity.setBirthDate(calendar.getTime());
                        patientBirthDateEditTextView.setText(Utils.getFormattedDate(calendar.getTime()));
                    }, currentYear, currentMonth, currentDay);
                    mDatePicker.setTitle(R.string.birthdate_label);
                    mDatePicker.show();
                });


            }
        });

        // Get the id from the bundle and fetch data from ViewModel
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
                onBackPressed();
                break;
        }
        return true;
    }

    // Save the details of the patient
    private boolean saveDetails() {
        if(!TextUtils.isEmpty(patientNameEditTextView.getText().toString())){
            viewModel.savePatientData(patientNameEditTextView.getText().toString(),Utils.getParsedDate(patientBirthDateEditTextView.getText().toString()),patientGenderSpinner.getSelectedItem().toString());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(saveDetails()){
            finish();
        } else {
            Toast.makeText(this,R.string.invalid_name_error_text,Toast.LENGTH_LONG).show();
        }
    }

    // Save isMode if config changes
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDIT_MODE,isEditMode);
        super.onSaveInstanceState(outState);
    }
}
