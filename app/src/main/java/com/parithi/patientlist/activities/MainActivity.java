package com.parithi.patientlist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parithi.patientlist.R;
import com.parithi.patientlist.fragments.PatientListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.home_title);

        // Loading PatientListFragment to the container
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PatientListFragment())
                .commit();
    }
}
