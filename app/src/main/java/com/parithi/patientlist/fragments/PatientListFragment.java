package com.parithi.patientlist.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parithi.patientlist.R;
import com.parithi.patientlist.activities.PatientDetailActivity;
import com.parithi.patientlist.database.patients.PatientEntity;
import com.parithi.patientlist.repositories.PatientRepository;
import com.parithi.patientlist.utils.Constants;
import com.parithi.patientlist.utils.Utils;
import com.parithi.patientlist.viewmodels.PatientListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.parithi.patientlist.utils.Constants.NETWORK_CODES.FAILURE;

public class PatientListFragment extends Fragment {

    // Variables for the views and adapters
    @BindView(R.id.patient_list_recylerview) public RecyclerView patientListRecyclerView;
    @BindView(R.id.progress_bar) public ProgressBar progressBar;
    @BindView(R.id.message_textview) public TextView messageTextView;

    private List<PatientEntity> patientList = new ArrayList<>();
    private PatientListAdapter patientListAdapter;

    private PatientListViewModel patientListViewModel;
    private String searchQuery;

    private PatientRepository.SORT_METHODS currentSortMethod = PatientRepository.SORT_METHODS.SORT_BY_NAME;
    private String[] sortByLabels = {"By Name","By Birth Date","By Gender"};

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
        View inflatedView = inflater.inflate(R.layout.fragment_patient_list, container, false);
        ButterKnife.bind(this,inflatedView);
        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    // Once view is created, initialize the required views
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientListRecyclerView.setHasFixedSize(true);
        patientListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        patientListAdapter = new PatientListAdapter();
        patientListRecyclerView.setAdapter(patientListAdapter);

        patientListViewModel = ViewModelProviders.of(this).get(PatientListViewModel.class);

        patientListViewModel.getErrorCode().observe(this,networkCode -> {
            if(networkCode == FAILURE){
                showError(Constants.UNABLE_TO_RETREIVE_DATA);
            }
        });

        patientListViewModel.getShowLoading().observe(this ,showLoading->{
            if(showLoading){
                progressBar.setVisibility(View.VISIBLE);
                messageTextView.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                messageTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateData() {
        patientListViewModel.fetchPatientDataFromDb(currentSortMethod,searchQuery);
        patientListViewModel.getPatientData().observe(this, patientEntities -> {
            Log.d("TROUBLE","data updated");
            notifyData(patientEntities);
        });
    }

    private void showError(int errorStringResourceId) {
        Toast.makeText(getActivity(),errorStringResourceId,Toast.LENGTH_LONG).show();
    }

    // Prepare the SearchView
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.patient_list, menu);

        MenuItem searchItem = menu.findItem( R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_label));
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                Log.d(PatientListFragment.class.getSimpleName(), "onQueryTextChange: Searching : " + newQuery);
                if (TextUtils.isEmpty(newQuery)) {
                    searchQuery = null;
                } else {
                    searchQuery = newQuery;
                }
                updateData();
                return true;
            }
        });

        searchView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && getActivity()!=null){
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            if(getActivity()!=null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else if (item.getItemId() == R.id.filter){
            showSortAlert();
        }
        return true;
    }


    public void notifyData(List<PatientEntity> updatedPatientData) {
        if(updatedPatientData !=null) {
            if(updatedPatientData.size() > 0){
                patientList.clear();
                patientList.addAll(updatedPatientData);
                patientListRecyclerView.setVisibility(View.VISIBLE);
                patientListAdapter.notifyDataSetChanged();
                messageTextView.setVisibility(View.GONE);
            } else {
                patientListRecyclerView.setVisibility(View.GONE);
                messageTextView.setText(R.string.no_data_found_label);
                if(progressBar.getVisibility()==View.GONE){
                    messageTextView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            messageTextView.setText(R.string.unable_to_refresh_data);
        }
    }

    private void showSortAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by_label);
        builder.setSingleChoiceItems(sortByLabels,currentSortMethod.getValue(), (dialog, which) -> {
            currentSortMethod = PatientRepository.SORT_METHODS.values()[which];
            updateData();
            dialog.dismiss();
        });
        builder.show();
    }

    // Adapter for RecyclerView for showing the data on the list
    public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {

        public  class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.patient_name_textview) TextView patientNameTextView;
            @BindView(R.id.patient_birth_date_textview) TextView patientBirthDateTextView;
            @BindView(R.id.profile_imageview) CircleImageView patientImageView;
            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this,v);
            }
        }

        public PatientListAdapter() {}

        @Override
        public PatientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.patientNameTextView.setText(patientList.get(position).getName());
            holder.patientBirthDateTextView.setText(patientList.get(position).getGender());
            if(getActivity()!=null){
                Glide.with(getActivity()).load(Utils.getImageUrl(128,patientList.get(position).getId())).into(holder.patientImageView);
            }
            holder.itemView.setOnClickListener(v -> {
                Intent patientDetailIntent = new Intent(getActivity(),PatientDetailActivity.class);
                patientDetailIntent.putExtra(Constants.PATIENT_ID, patientList.get(position).getId());
                startActivity(patientDetailIntent);
            });
        }

        @Override
        public int getItemCount() {
            return (patientList !=null) ? patientList.size() : 0;
        }
    }
}
