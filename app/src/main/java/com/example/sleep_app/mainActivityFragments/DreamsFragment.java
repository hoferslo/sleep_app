package com.example.sleep_app.mainActivityFragments;

import android.content.Context;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.sleep_app.Dream;
import com.example.sleep_app.MainActivity;
import com.example.sleep_app.fragments.DreamDetailsFragment;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentDreamsBinding;

import com.example.sleep_app.fragments.FilterDreamsDialogFragment;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DreamsFragment extends Fragment implements DreamDetailsFragment.OnDialogDismissListener {

    FragmentDreamsBinding binding;
    public int scrollProgress;
    List<Dream> dreamList;


    public DreamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDreamsBinding.inflate(inflater, container, false);

        scrollProgress = 0;

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the query submission if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the query text change, filter the list, and update the adapter
                filterDreams(newText);
                return true;
            }
        });

        binding.searchView.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                // SearchView lost focus, clear the focus
                binding.searchView.clearFocus();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        DreamsAccess dreamsAccess = new DreamsAccess(requireContext());
        dreamsAccess.open();
        dreamList = dreamsAccess.getDreams();
        dreamsAccess.close();

        getDreams();
    }

    public boolean onBackPressed(){
        if (binding.searchView.hasFocus()) {
            binding.searchView.clearFocus();
            return false;
        } else {
            // If SearchView is not focused, proceed with default back button behavior
            return true;
        }
    }


    private void getDreams(){

        ArrayList<Dream> dreamArrayList = new ArrayList<>(dreamList);
        ArrayAdapter<Dream> adapter = new CustomAdapter(requireContext(), R.layout.item_dream_layout, dreamArrayList);
        binding.scrollLv.setAdapter(adapter);
        binding.scrollLv.setDivider(null);
        binding.scrollLv.setSelection(scrollProgress);
        scrollProgress = 0;
        Log.d("DreamsFragment onResume", "getDreams() method was called");
    }

    private void filterDreams(String query) {
        ArrayList<Dream> dreamArrayList = new ArrayList<>();;
        if (dreamList != null) {
            dreamArrayList = new ArrayList<>(dreamList);
        }
        ArrayList<Dream> filteredList = new ArrayList<>();
        for (Dream dream : dreamArrayList) {
            if (dream.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(dream);
            } else if (dream.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(dream);
            }
        }
        ArrayAdapter<Dream> adapter = new CustomAdapter(requireContext(), R.layout.item_dream_layout, filteredList);
        binding.scrollLv.setAdapter(adapter);
    }

    private void filterDreams(int lucidityStart, int lucidityEnd, int clarityStart, int clarityEnd, LocalDateTime dateStart, LocalDateTime dateEnd) {
        ArrayList<Dream> dreamArrayList = new ArrayList<>();
        if (dreamList != null) {
            dreamArrayList = new ArrayList<>(dreamList);
        }
        ArrayList<Dream> filteredList = new ArrayList<>();
        for (Dream dream : dreamArrayList) {
            int dreamLucidity = dream.getLucidity();
            int dreamClarity = dream.getClarity();
            LocalDateTime dreamDate = dream.getDateCreated();

            if (dreamLucidity >= lucidityStart && dreamLucidity <= lucidityEnd
                    && dreamClarity >= clarityStart && dreamClarity <= clarityEnd
                    && dreamDate.isAfter(dateStart) && dreamDate.isBefore(dateEnd)) {
                filteredList.add(dream);
            }
        }

        ArrayAdapter<Dream> adapter = new CustomAdapter(requireContext(), R.layout.item_dream_layout, filteredList);
        binding.scrollLv.setAdapter(adapter);
    }



    @Override
    public void onDialogDismissed() {
        onResume();
    }

    private class CustomAdapter extends ArrayAdapter<Dream> {
        private final LayoutInflater inflater;
        private final int resource;
        private final ArrayList<Dream> objects;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Dream> objects) {
            super(context, resource, objects);
            this.inflater = LayoutInflater.from(context);
            this.resource = resource;
            this.objects = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Inflate your custom layout for each item
            if (convertView == null) {
                convertView = inflater.inflate(resource, parent, false);
            }

            MainActivity.makeDreamView(convertView, objects.get(position));

            convertView.setOnClickListener(v -> {
                scrollProgress = binding.scrollLv.getFirstVisiblePosition();
                showDreamDetailsDialog(objects.get(position));

            });

            return convertView;
        }
    }
    private void showDreamDetailsDialog(Dream clickedDream) {
        DreamDetailsFragment dialogFragment = DreamDetailsFragment.newInstance(clickedDream);
        dialogFragment.setOnDialogDismissListener(this);
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "dream_details_dialog");
    }

    public void showFilterDialog(){
        FilterDreamsDialogFragment filterDreamsDialog = new FilterDreamsDialogFragment();
        filterDreamsDialog.setFilterDreamsDialogListener(new FilterDreamsDialogFragment.FilterDreamsDialogListener() {
            @Override
            public void onDialogPositiveClick(int lucidityStart, int lucidityEnd, int clarityStart, int clarityEnd, LocalDateTime dateStart, LocalDateTime dateEnd) {
                filterDreams(lucidityStart, lucidityEnd, clarityStart, clarityEnd, dateStart, dateEnd);
            }

            @Override
            public void onDialogNegativeClick() {

            }
        });
        filterDreamsDialog.show(getParentFragmentManager(), "filterDreamsDialog");
    }
}