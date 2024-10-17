package com.example.sleep_app.mainActivityFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sleep_app.Dream;
import com.example.sleep_app.MainActivity;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentDreamsBinding;
import com.example.sleep_app.enums.SortOption;
import com.example.sleep_app.fragments.DreamDetailsFragment;
import com.example.sleep_app.fragments.FilterDreamsDialogFragment;
import com.example.sleep_app.fragments.SortDreamsDialogFragment;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DreamsFragment extends Fragment implements DreamDetailsFragment.OnDialogDismissListener {

    FragmentDreamsBinding binding;
    public int scrollProgress;
    List<Dream> dreamList;

    String query;
    Integer lucidityStart;
    Integer lucidityEnd;
    Integer clarityStart;
    Integer clarityEnd;
    Integer happinessStart;
    Integer happinessEnd;
    Integer recurringDream;
    Integer nightmare;
    LocalDateTime dateStart;
    LocalDateTime dateEnd;

    SortOption sortOption = SortOption.DATE;
    boolean sortOrder = false;

    public DreamsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDreamsBinding.inflate(inflater, container, false);

        scrollProgress = 0;

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDreams(newText, lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd, sortOption, sortOrder);
                return true;
            }
        });

        binding.searchView.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                binding.searchView.clearFocus();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.scrollLv.setLayoutManager(layoutManager);

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

    public boolean onBackPressed() {
        if (binding.searchView.hasFocus()) {
            binding.searchView.clearFocus();
            return false;
        } else {
            return true;
        }
    }


    private void getDreams() {

        filterDreams(query, lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd, sortOption, sortOrder);
        binding.scrollLv.scrollToPosition(scrollProgress);
        scrollProgress = 0;
    }

    private void filterDreams(String query, Integer lucidityStart, Integer lucidityEnd, Integer clarityStart, Integer clarityEnd, Integer happinessStart, Integer happinessEnd, Integer recurringDream, Integer nightmare, LocalDateTime dateStart, LocalDateTime dateEnd, SortOption sortOption, boolean sortOrder) {
        this.query = query;
        this.lucidityStart = lucidityStart;
        this.lucidityEnd = lucidityEnd;
        this.clarityStart = clarityStart;
        this.clarityEnd = clarityEnd;
        this.happinessStart = happinessStart;
        this.happinessEnd = happinessEnd;
        this.recurringDream = recurringDream;
        this.nightmare = nightmare;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;

        ArrayList<Dream> dreamArrayList = new ArrayList<>();
        if (dreamList != null) {
            dreamArrayList = new ArrayList<>(dreamList);
        }
        if (query != null && query.equals("")) {
            query = null;
        }

        // Apply initial filtering based on text query and dream properties
        ArrayList<Dream> filteredList = filterDreamsBasedOnCriteria(dreamArrayList, query, lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd);

        // Sort the filtered list based on chosen option and order
        filteredList.sort((dream1, dream2) -> {
            int comparisonResult = 0;
            switch (sortOption) {
                case LUCIDITY:
                    comparisonResult = Integer.compare(dream1.getLucidity(), dream2.getLucidity());
                    break;
                case CLARITY:
                    comparisonResult = Integer.compare(dream1.getClarity(), dream2.getClarity());
                    break;
                case HAPPINESS:
                    comparisonResult = Integer.compare(dream1.getHappiness(), dream2.getHappiness());
                    break;
                case DATE:
                    comparisonResult = dream1.getDateCreated().compareTo(dream2.getDateCreated());
                    break;
                case ALPHABETICALLY:
                    comparisonResult = dream1.getTitle().compareToIgnoreCase(dream2.getTitle());
                    break;
            }
            return sortOrder ? comparisonResult : -comparisonResult; // Reverse order if sortOrder is false
        });

        CustomAdapter adapter = new CustomAdapter(requireContext(), filteredList);
        binding.scrollLv.setAdapter(adapter);
    }

    // Helper method for initial filtering
    private ArrayList<Dream> filterDreamsBasedOnCriteria(ArrayList<Dream> dreams, String query, Integer lucidityStart, Integer lucidityEnd, Integer clarityStart, Integer clarityEnd, Integer happinessStart, Integer happinessEnd, Integer recurringDream, Integer nightmare, LocalDateTime dateStart, LocalDateTime dateEnd) {
        ArrayList<Dream> filteredList = new ArrayList<>();
        for (Dream dream : dreams) {
            boolean matchesQuery = query == null ||
                    (dream.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            dream.getDescription().toLowerCase().contains(query.toLowerCase()));

            boolean matchesFilters = (lucidityStart == null || (dream.getLucidity() >= lucidityStart && dream.getLucidity() <= lucidityEnd)) &&
                    (clarityStart == null || (dream.getClarity() >= clarityStart && dream.getClarity() <= clarityEnd)) &&
                    (happinessStart == null || (dream.getHappiness() >= happinessStart && dream.getHappiness() <= happinessEnd)) &&
                    (recurringDream == null || recurringDream == 2 || (dream.getRecurringDream() == recurringDream)) &&
                    (nightmare == null || nightmare == 2 || (dream.getNightmare() == nightmare)) &&
                    (dateStart == null || (dream.getDateCreated().isAfter(dateStart) && dream.getDateCreated().isBefore(dateEnd)));

            if (matchesQuery && matchesFilters) {
                filteredList.add(dream);
            }
        }
        return filteredList;
    }


    @Override
    public void onDialogDismissed() {
        onResume();
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private final LayoutInflater inflater;
        private final ArrayList<Dream> objects;
        private final Context context;

        public CustomAdapter(@NonNull Context context, @NonNull ArrayList<Dream> objects) {
            this.inflater = LayoutInflater.from(context);
            this.objects = objects;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_dream_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Dream dream = objects.get(position);
            MainActivity.makeDreamView(holder.itemView, dream, context);
            holder.itemView.setOnClickListener(v -> {
                scrollProgress = binding.scrollLv.getChildLayoutPosition(holder.itemView);
                showDreamDetailsDialog(objects.get(position));
            });
        }

        @Override
        public int getItemCount() {
            return objects.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


    private void showDreamDetailsDialog(Dream clickedDream) {
        DreamDetailsFragment dialogFragment = DreamDetailsFragment.newInstance(clickedDream);
        dialogFragment.setOnDialogDismissListener(this);
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "dream_details_dialog");
    }

    public void showFilterDialog() {
        FilterDreamsDialogFragment filterDreamsDialog = FilterDreamsDialogFragment.newInstance(lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd);
        filterDreamsDialog.setFilterDreamsDialogListener(new FilterDreamsDialogFragment.FilterDreamsDialogListener() {
            @Override
            public void onDialogPositiveClick(Integer lucidityStart, Integer lucidityEnd, Integer clarityStart, Integer clarityEnd, Integer happinessStart, Integer happinessEnd, Integer recurringDream, Integer nightmare, LocalDateTime dateStart, LocalDateTime dateEnd) {
                filterDreams(query, lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd, sortOption, sortOrder);
            }

            @Override
            public void onDialogNegativeClick() {

            }
        });
        filterDreamsDialog.show(getParentFragmentManager(), "filterDreamsDialog");
    }

    public void showSortDialog() {
        SortDreamsDialogFragment sortDreamsDialog = SortDreamsDialogFragment.newInstance(sortOption, sortOrder);
        sortDreamsDialog.setSortDreamsDialogListener(new SortDreamsDialogFragment.SortDreamsDialogListener() {

            @Override
            public void onDialogPositiveClick(SortOption sortOption, boolean sortOrder) {
                DreamsFragment.this.sortOption = sortOption;
                DreamsFragment.this     .sortOrder = sortOrder;
                filterDreams(query, lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd, sortOption, sortOrder);
            }

            @Override
            public void onDialogNegativeClick() {

            }
        });
        sortDreamsDialog.show(getParentFragmentManager(), "filterDreamsDialog");
    }
}