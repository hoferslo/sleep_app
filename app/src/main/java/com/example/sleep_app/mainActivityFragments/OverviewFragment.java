package com.example.sleep_app.mainActivityFragments;

import static java.lang.Float.NaN;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sleep_app.Dream;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentOverviewBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;
import com.scwang.wave.MultiWaveHeader;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class OverviewFragment extends Fragment {

    FragmentOverviewBinding binding;
    private ArrayAdapter<String> adapter;
    private List<Dream> dreams;
    private LocalDateTime now;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        now = LocalDateTime.now();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        createStats();
        Log.d("OverviewFragment onResume", "createStats() method was called");
    }

    private void createStats(){
        binding.scrollLl.removeAllViews();
        DreamsAccess dreamsAccess = new DreamsAccess(requireContext());
        dreamsAccess.open();
        dreams = dreamsAccess.getDreams();
        dreamsAccess.close();

        int thisMonthDreamsNumber = getMonthDreamsCount(now, false);

        {
            View dreamStatsLayout = LayoutInflater.from(requireContext()).inflate(R.layout.item_dream_stats_layout, binding.scrollLl, false);
            TextView title = dreamStatsLayout.findViewById(R.id.titleTv);
            TextView description = dreamStatsLayout.findViewById(R.id.DescriptionTv);
            MultiWaveHeader waveView = dreamStatsLayout.findViewById(R.id.waveHeader);
            title.setText("Number of dreams from this month");
            description.setText(thisMonthDreamsNumber + "/" + now.getDayOfMonth());

            waveView.setProgress(getWaveViewProgress(thisMonthDreamsNumber, now.getDayOfMonth()));
            binding.scrollLl.addView(dreamStatsLayout);
        }

        int thisMonthLucidDreamsNumber = getMonthDreamsCount(now, true);

        {
            View dreamStatsLayout = LayoutInflater.from(requireContext()).inflate(R.layout.item_dream_stats_layout, binding.scrollLl, false);
            TextView title = dreamStatsLayout.findViewById(R.id.titleTv);
            TextView description = dreamStatsLayout.findViewById(R.id.DescriptionTv);
            MultiWaveHeader waveView = dreamStatsLayout.findViewById(R.id.waveHeader);
            title.setText("Number of lucid dreams from this month's dreams");
            description.setText(thisMonthLucidDreamsNumber + "/" + thisMonthDreamsNumber);
            waveView.setProgress(getWaveViewProgress(thisMonthLucidDreamsNumber, thisMonthDreamsNumber));
            binding.scrollLl.addView(dreamStatsLayout);
        }

        int pastMonthDreamsNumber = getMonthDreamsCount(now.minusMonths(1), false);

        {
            View dreamStatsLayout = LayoutInflater.from(requireContext()).inflate(R.layout.item_dream_stats_layout, binding.scrollLl, false);
            TextView title = dreamStatsLayout.findViewById(R.id.titleTv);
            TextView description = dreamStatsLayout.findViewById(R.id.DescriptionTv);
            MultiWaveHeader waveView = dreamStatsLayout.findViewById(R.id.waveHeader);
            title.setText("Number of dreams from the past month");
            description.setText(pastMonthDreamsNumber + "/" + YearMonth.from(now.minusMonths(1)).lengthOfMonth());
            waveView.setProgress(getWaveViewProgress(pastMonthDreamsNumber, YearMonth.from(now.minusMonths(1)).lengthOfMonth()));
            binding.scrollLl.addView(dreamStatsLayout);
        }

        int pastMonthLucidDreamsNumber = getMonthDreamsCount(now.minusMonths(1), true);

        {
            View dreamStatsLayout = LayoutInflater.from(requireContext()).inflate(R.layout.item_dream_stats_layout, binding.scrollLl, false);
            TextView title = dreamStatsLayout.findViewById(R.id.titleTv);
            TextView description = dreamStatsLayout.findViewById(R.id.DescriptionTv);
            MultiWaveHeader waveView = dreamStatsLayout.findViewById(R.id.waveHeader);
            title.setText("Number of lucid dreams from dreams in the past month");
            description.setText(pastMonthLucidDreamsNumber + "/" + pastMonthDreamsNumber);
            waveView.setProgress(getWaveViewProgress(pastMonthLucidDreamsNumber, pastMonthDreamsNumber));
            binding.scrollLl.addView(dreamStatsLayout);
        }

    }

    public int getMonthDreamsCount(LocalDateTime dateTime, boolean lucid){
        int dreamsThisMonth = 0;
        for (Dream dream:dreams) {
            if (dateTime.getMonth() == dream.getDateCreated().getMonth()) {
                if (lucid){
                    if (dream.getLucidity()>0){
                        dreamsThisMonth++;
                    }
                } else {
                    dreamsThisMonth++;
                }
            }
        }
        return dreamsThisMonth;
    }

    public float getWaveViewProgress(float first, float second){
        float waveViewProgress = first / second;
        if (waveViewProgress>1){
            waveViewProgress=1;
        }
        if (Float.isNaN(waveViewProgress)){
            waveViewProgress=0;
        }
        return waveViewProgress;
    }

}
