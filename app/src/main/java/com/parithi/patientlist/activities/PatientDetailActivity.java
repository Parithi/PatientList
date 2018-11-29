package com.parithi.patientlist.activities;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

    // variables to store patient name, gender and birthdate
    String patientId;
    boolean isEditMode;

    @BindView(R.id.patient_name_textview) EditText patientNameEditTextView;
    @BindView(R.id.patient_birth_date_textview) TextView patientBirthDateEditTextView;
    @BindView(R.id.patient_gender_textview) Spinner patientGenderSpinner;
    @BindView(R.id.profile_imageview) ImageView patientImageView;

    private ArrayList<String> genderArray = Utils.getGenderArray();

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

        patientGenderSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.gender_spinner_item, genderArray));

        viewModel = ViewModelProviders.of(this).get(PatientEditViewModel.class);
        viewModel.getPatientDetail().observe(this,patientEntity -> {
            if(patientEntity!=null && !isEditMode){
                patientNameEditTextView.setText(patientEntity.getName());
                patientBirthDateEditTextView.setText(Utils.getFormattedDate(patientEntity.getBirthDate()));
                patientGenderSpinner.setSelection(genderArray.indexOf(patientEntity.getGender().trim()));
                Glide.with(this).load("https://picsum.photos/g/128?random="+patientEntity.getId()).into(patientImageView);

                patientBirthDateEditTextView.setOnClickListener(v -> {
                    Calendar mcurrentDate= new GregorianCalendar();
                    mcurrentDate.setTime(patientEntity.getBirthDate());
                    int mYear=mcurrentDate.get(Calendar.YEAR);
                    int mMonth=mcurrentDate.get(Calendar.MONTH);
                    int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker=new DatePickerDialog(PatientDetailActivity.this, (view, year, month, dayOfMonth) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        patientEntity.setBirthDate(calendar.getTime());
                        patientBirthDateEditTextView.setText(Utils.getFormattedDate(calendar.getTime()));
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle(R.string.birthdate_label);
                    mDatePicker.show();
                });


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
                saveDetails();
                finish();
                break;
        }
        return true;
    }

    private void saveDetails() {
        viewModel.savePatientData(patientNameEditTextView.getText().toString(),Utils.getParsedDate(patientBirthDateEditTextView.getText().toString()),patientGenderSpinner.getSelectedItem().toString());
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
