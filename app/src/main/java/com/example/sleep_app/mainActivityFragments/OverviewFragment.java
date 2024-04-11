package com.example.sleep_app.mainActivityFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sleep_app.Dream;
import com.example.sleep_app.MainActivity;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.FragmentOverviewBinding;
import com.example.sleep_app.fragments.DreamDetailsFragment;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;
import com.example.sleep_app.tools.PrefsHelper;
import com.scwang.wave.MultiWaveHeader;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;

public class OverviewFragment extends Fragment implements DreamDetailsFragment.OnDialogDismissListener {

    FragmentOverviewBinding binding;
    private ArrayAdapter<String> adapter;
    private List<Dream> dreams;
    private LocalDateTime now;
    private PrefsHelper prefsHelper;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOverviewBinding.inflate(inflater, container, false);

        now = LocalDateTime.now();

        prefsHelper = new PrefsHelper(requireContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            createStats();
        }
    }

    private void createStats() {
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

        {
            if (prefsHelper.isRandomDreamEnabled() && !dreams.isEmpty()) {
                Dream dream = dreams.get(new Random().nextInt(dreams.size()));

                LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View RandomDreamLl = inflater.inflate(R.layout.item_random_dream, null, false);

                View parentView = inflater.inflate(R.layout.item_dream_layout, null, false);
                View dreamView = MainActivity.makeDreamView(parentView, dream, requireContext());

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, (int) (8 * getResources().getDisplayMetrics().density + 0.5f), 0, 0);
                dreamView.findViewById(R.id.itemDreamLl).setLayoutParams(layoutParams);

                dreamView.setOnClickListener(v -> {
                    showDreamDetailsDialog(dream);
                });

                LinearLayout Ll = RandomDreamLl.findViewById(R.id.RandomDreamLl);
                Ll.addView(dreamView);
                binding.scrollLl.addView(RandomDreamLl);
            }
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

    public int getMonthDreamsCount(LocalDateTime dateTime, boolean lucid) {
        int dreamsThisMonth = 0;
        for (Dream dream : dreams) {
            if (dateTime.getMonth() == dream.getDateCreated().getMonth()) {
                if (lucid) {
                    if (dream.getLucidity() > 0) {
                        dreamsThisMonth++;
                    }
                } else {
                    dreamsThisMonth++;
                }
            }
        }
        return dreamsThisMonth;
    }

    public float getWaveViewProgress(float first, float second) {
        float waveViewProgress = first / second;
        if (waveViewProgress > 1) {
            waveViewProgress = 1;
        }
        if (Float.isNaN(waveViewProgress)) {
            waveViewProgress = 0;
        }
        return waveViewProgress;
    }

    private void showDreamDetailsDialog(Dream clickedDream) {
        DreamDetailsFragment dialogFragment = DreamDetailsFragment.newInstance(clickedDream);
        dialogFragment.setOnDialogDismissListener(this);
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "dream_details_dialog");
    }

    @Override
    public void onDialogDismissed() {
        onResume();
    }
}
