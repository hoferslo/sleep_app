package com.example.sleep_app.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sleep_app.MainActivity;
import com.example.sleep_app.R;
import com.example.sleep_app.databinding.CustomFilterDreamsDialogBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsHelper;

import java.time.LocalDateTime;

public class FilterDreamsDialogFragment extends DialogFragment {

    CustomFilterDreamsDialogBinding binding;
    LocalDateTime selectedDateTime;
    //add parameters for saving the previous filter
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
    boolean booleanFiltersEnabled;

    // Interface for the callback
    public interface FilterDreamsDialogListener {
        void onDialogPositiveClick(Integer lucidityStart, Integer lucidityEnd, Integer clarityStart, Integer clarityEnd, Integer happinessStart, Integer happinessEnd, Integer recurringDream, Integer nightmare, LocalDateTime dateStart, LocalDateTime dateEnd, boolean booleanFiltersEnabled);

        void onDialogNegativeClick();
    }

    public static FilterDreamsDialogFragment newInstance(Integer lucidityStart, Integer lucidityEnd, Integer clarityStart, Integer clarityEnd, Integer happinessStart, Integer happinessEnd, Integer recurringDream, Integer nightmare, LocalDateTime dateStart, LocalDateTime dateEnd, boolean booleanFiltersEnabled) {
        FilterDreamsDialogFragment filterDreamsDialog = new FilterDreamsDialogFragment();
        filterDreamsDialog.setStartingValues(lucidityStart, lucidityEnd, clarityStart, clarityEnd, happinessStart, happinessEnd, recurringDream, nightmare, dateStart, dateEnd, booleanFiltersEnabled);
        return filterDreamsDialog;
    }

    public void setStartingValues(Integer lucidityStart, Integer lucidityEnd, Integer clarityStart, Integer clarityEnd, Integer happinessStart, Integer happinessEnd, Integer recurringDream, Integer nightmare, LocalDateTime dateStart, LocalDateTime dateEnd, boolean booleanFiltersEnabled) {
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
        this.booleanFiltersEnabled = booleanFiltersEnabled;
    }

    private FilterDreamsDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.FullScreenDialog);

        binding = CustomFilterDreamsDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setView();

        builder.setView(view);

        return builder.create();
    }

    public void setView() {
        binding.seekBarClarity.addOnChangeListener((slider, value, fromUser) -> {
            binding.seekBarClarityStartTv.setText(String.valueOf(slider.getValues().get(0).intValue()));
            binding.seekBarClarityEndTv.setText(String.valueOf(slider.getValues().get(1).intValue()));
        });

        binding.seekBarLucidity.addOnChangeListener((slider, value, fromUser) -> {
            binding.seekBarLucidityStartTv.setText(String.valueOf(slider.getValues().get(0).intValue()));
            binding.seekBarLucidityEndTv.setText(String.valueOf(slider.getValues().get(1).intValue()));
        });

        binding.seekBarHappiness.addOnChangeListener((slider, value, fromUser) -> {
            binding.seekBarHappinessStartTv.setText(String.valueOf(slider.getValues().get(0).intValue()));
            binding.seekBarHappinessEndTv.setText(String.valueOf(slider.getValues().get(1).intValue()));
            binding.happinessStartIv.setImageResource(MainActivity.getHappinessImage(slider.getValues().get(0).intValue()));
            binding.happinessEndIv.setImageResource(MainActivity.getHappinessImage(slider.getValues().get(1).intValue()));
        });

        binding.iconFilterEnableTv.setOnClickListener(v -> {
            booleanFiltersEnabled = !booleanFiltersEnabled;
            handleBooleanFilterLogic(booleanFiltersEnabled);
        });

        binding.btnPositive.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogPositiveClick(
                        binding.seekBarLucidity.getValues().get(0).intValue(),
                        binding.seekBarLucidity.getValues().get(1).intValue(),
                        binding.seekBarClarity.getValues().get(0).intValue(),
                        binding.seekBarClarity.getValues().get(1).intValue(),
                        binding.seekBarHappiness.getValues().get(0).intValue(),
                        binding.seekBarHappiness.getValues().get(1).intValue(),
                        recurringDream == null ? null : binding.recurringDreamIv.isSelected() ? 1 : 0,
                        nightmare == null ? null : binding.nightmareIv.isSelected() ? 1 : 0,
                        DreamsHelper.dateStringToDateTime(binding.textViewDateStart.getText().toString()),
                        selectedDateTime,
                        booleanFiltersEnabled);
                dismiss();
            }
        });

        binding.btnNegative.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogNegativeClick();
                dismiss();
            }
        });

        binding.btnReset.setOnClickListener(v -> {
            setStartingValues(null, null, null, null, null, null, null, null, null, null, false);
            setView();
        });

        binding.DateStartLl.setOnClickListener(v -> showDatePickerDialogStart());
        binding.DateEndLl.setOnClickListener(v -> showDatePickerDialogEnd());


        handleBooleanFilterLogic(booleanFiltersEnabled);

        if (lucidityStart == null && lucidityEnd == null) {
            binding.seekBarLucidity.setValues(0.0f, 100.0f);
        } else {
            binding.seekBarLucidity.setValues(Float.valueOf(lucidityStart), Float.valueOf(lucidityEnd));
        }

        if (clarityStart == null && clarityEnd == null) {
            binding.seekBarClarity.setValues(0.0f, 100.0f);
        } else {
            binding.seekBarClarity.setValues(Float.valueOf(clarityStart), Float.valueOf(clarityEnd));
        }

        if (happinessStart == null && happinessEnd == null) {
            binding.seekBarHappiness.setValues(0.0f, 100.0f);
        } else {
            binding.seekBarHappiness.setValues(Float.valueOf(happinessStart), Float.valueOf(happinessEnd));
        }

        if (recurringDream != null) {
            binding.recurringDreamIv.setSelected(recurringDream == 1);
        }

        if (nightmare != null) {
            binding.nightmareIv.setSelected(nightmare == 1);
        }

        if (dateStart == null) {
            binding.textViewDateStart.setText(DreamsHelper.DateTimeToDateString(LocalDateTime.of(1900, 1, 1, 1, 1)));
        } else {
            binding.textViewDateStart.setText(DreamsHelper.DateTimeToDateString(dateStart));
        }

        if (dateEnd == null) {
            LocalDateTime now = LocalDateTime.now();
            binding.textViewDateEnd.setText(DreamsHelper.DateTimeToDateString(now));
            this.selectedDateTime = now;
        } else {
            binding.textViewDateEnd.setText(DreamsHelper.DateTimeToDateString(dateEnd));
            this.selectedDateTime = dateEnd;
        }
    }

    public void handleBooleanFilterLogic(boolean booleanFiltersEnabled) {
        if (booleanFiltersEnabled) {
            recurringDream = binding.recurringDreamIv.isSelected() ? 1 : 0;
            nightmare = binding.nightmareIv.isSelected() ? 1 : 0;
            binding.recurringDreamIv.setClickable(true);
            binding.nightmareIv.setClickable(true);
            binding.iconsLl.setBackgroundResource(R.drawable.rounded_background_darker_with_border);
            binding.recurringDreamIv.handleSelection();
            binding.nightmareIv.handleSelection();
            binding.iconFilterEnableTv.setText("Stop using this filter");
        } else {
            binding.recurringDreamIv.setClickable(false);
            binding.nightmareIv.setClickable(false);
            binding.recurringDreamIv.setColor(R.color.sleepyBlueSeeThrough);
            binding.nightmareIv.setColor(R.color.sleepyBlueSeeThrough);
            binding.iconsLl.setBackgroundResource(R.drawable.rounded_background_darkest_with_border);
            recurringDream = null;
            nightmare = null;
            binding.iconFilterEnableTv.setText("Start using this filter");
        }
    }

    public void setFilterDreamsDialogListener(FilterDreamsDialogListener listener) {
        this.listener = listener;
    }

    private void showDatePickerDialogStart() {
        // Get current date
        LocalDateTime currentDateTime = LocalDateTime.now();
        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue() - 1; // Adjust for zero-indexed months
        int day = currentDateTime.getDayOfMonth();

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {

                    //this.selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute, second);
                    binding.textViewDateStart.setText(DreamsHelper.DateTimeToDateString(LocalDateTime.of(year1, month1 + 1, dayOfMonth, 0, 0, 0)));

                },
                year,
                month,
                day
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private void showDatePickerDialogEnd() {
        // Get current date
        LocalDateTime currentDateTime = DreamsHelper.dateStringToDateTime(binding.textViewDateEnd.getText().toString());
        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue() - 1; // Adjust for zero-indexed months
        int day = currentDateTime.getDayOfMonth();
        int hour = currentDateTime.getHour();
        int minute = currentDateTime.getMinute();
        int second = currentDateTime.getSecond();

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {

                    this.selectedDateTime = LocalDateTime.of(year1, month1 + 1, dayOfMonth, hour, minute, second);
                    binding.textViewDateEnd.setText(DreamsHelper.DateTimeToDateString(selectedDateTime));

                },
                year,
                month,
                day
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }

}
