package com.parithi.patientlist;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.parithi.patientlist.activities.PatientDetailActivity;
import com.parithi.patientlist.database.patients.PatientEntity;
import com.parithi.patientlist.utils.Constants;
import com.parithi.patientlist.viewmodels.PatientEditViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientDetailActivityTest {

    private static final String TAG = "PatientDetailActivityTests";

    @Rule
    public ActivityTestRule<PatientDetailActivity> patientDetailActivityActivityTestRule = new ActivityTestRule<>(
            PatientDetailActivity.class,true,false);

    // Checking the patientdetail activity for null intents
    @Test
    public void checkForNullPatient(){
        patientDetailActivityActivityTestRule.launchActivity(null);
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.no_data_found_label);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(expectedTitle)));
    }

    // Checking the patientdetail activity for invalid data
    @Test
    public void checkForInvalidPatient(){
        Intent patientDetailIntent = new Intent();
        patientDetailIntent.putExtra(Constants.PATIENT_ID, "2723");
        patientDetailActivityActivityTestRule.launchActivity(new Intent(patientDetailIntent));
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.no_data_found_label);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class)))).check(matches(withText(expectedTitle)));
    }

}