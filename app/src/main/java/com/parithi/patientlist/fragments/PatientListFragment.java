package com.parithi.patientlist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parithi.patientlist.activities.PatientDetailActivity;
import com.parithi.patientlist.R;
import com.parithi.patientlist.helpers.PatientHelper;
import com.parithi.patientlist.models.Patient;
import com.parithi.patientlist.utils.Constants;

import java.util.List;

public class PatientListFragment extends Fragment implements
        PatientHelper.PatientHelperCallbackListener {

    // Holding reference to the PatientHelper
    PatientHelper patientManager = PatientHelper.getInstance();

    // Variables for the views and adapters
    private RecyclerView patientListRecyclerView;
    private List<Patient> patientList;
    private ProgressBar progressBar;
    private TextView messageTextView;
    private PatientListAdapter patientListAdapter;

    // Empty constructor
    public PatientListFragment() { }

    // newInstance() method for creating a new Instance of PatientListFragment with required params
    // Unused for now
    public static PatientListFragment newInstance() {
        PatientListFragment fragment = new PatientListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // Retaining the Instance of this fragment for config changes and
    // enabling menu options
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    // Inflating the layout for the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_list, container, false);
    }

    // Once view is created, initialize the required views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientListRecyclerView = view.findViewById(R.id.patient_list_recylerview);
        patientListRecyclerView.setHasFixedSize(true);
        patientListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        patientListAdapter = new PatientListAdapter();
        patientListRecyclerView.setAdapter(patientListAdapter);

        progressBar = view.findViewById(R.id.progress_bar);
        messageTextView = view.findViewById(R.id.message_textview);
    }

    // Once activity is created, link the current fragment to the PatientHelper
    // start processing the JSON data
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        patientManager.setCallbackListener(this);
        patientManager.retreivePatientData();
    }

    // On detaching fragment, remove the reference to patientManager
    @Override
    public void onDetach() {
        super.onDetach();
        patientManager.setCallbackListener(this);
    }

    // Clear all the data from memory when the fragment is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        PatientHelper.getInstance().clear();
    }


    // Method to clear the recyclerview
    @Override
    public void clearList() {
        if(patientList !=null) {
            patientList.clear();
            progressBar.setVisibility(View.VISIBLE);
            patientListRecyclerView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.GONE);
            patientListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void notifyData(List<com.parithi.patientlist.models.Patient> patientData) {
        progressBar.setVisibility(View.GONE);
        if(patientData!=null) {
            if(patientData.size() > 0){
                patientListRecyclerView.setVisibility(View.VISIBLE);
                patientList = patientData;
                patientListAdapter.notifyDataSetChanged();
            } else {
                messageTextView.setText(R.string.no_data_found_label);
                messageTextView.setVisibility(View.VISIBLE);
            }
        } else {
            messageTextView.setText(R.string.unable_to_retreive_data);
            messageTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(int message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    // Adapter for RecyclerView for showing the data on the list
    public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public TextView patientNameTextView;
            public TextView patientBirthDateTextview;
            public ViewHolder(View v) {
                super(v);
                patientNameTextView = v.findViewById(R.id.patient_name_textview);
                patientBirthDateTextview = v.findViewById(R.id.patient_birth_date_textview);
            }
        }

        public PatientListAdapter() {}

        @Override
        public PatientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.patientNameTextView.setText(patientList.get(position).getName());
            holder.patientBirthDateTextview.setText(patientList.get(position).getGender());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent patientDetailIntent = new Intent(getActivity(),PatientDetailActivity.class);
                    patientDetailIntent.putExtra(Constants.PATIENT_NAME,patientList.get(position).getName());
                    patientDetailIntent.putExtra(Constants.PATIENT_GENDER,patientList.get(position).getGender());
                    patientDetailIntent.putExtra(Constants.PATIENT_BIRTHDATE,patientList.get(position).getBirthDate().getTime());
                    startActivity(patientDetailIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (patientList !=null) ? patientList.size() : 0;
        }
    }
}
