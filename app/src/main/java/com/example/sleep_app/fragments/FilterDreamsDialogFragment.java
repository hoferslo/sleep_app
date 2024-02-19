package com.example.sleep_app.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sleep_app.R;

import com.example.sleep_app.databinding.CustomFilterDreamsDialogBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsHelper;

import java.time.LocalDateTime;

public class FilterDreamsDialogFragment extends DialogFragment {

    CustomFilterDreamsDialogBinding binding;

    // Interface for the callback
    public interface FilterDreamsDialogListener {
        void onDialogPositiveClick(int lucidityStart, int lucidityEnd, int clarityStart, int clarityEnd, LocalDateTime dateStart, LocalDateTime dateEnd);
        void onDialogNegativeClick();
    }

    private FilterDreamsDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // Inflate the custom layout
        binding = CustomFilterDreamsDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.seekBarClarity.addOnChangeListener((slider, value, fromUser) -> {
            binding.seekBarClarityStartTv.setText(String.valueOf(slider.getValues().get(0).intValue()));
            binding.seekBarClarityEndTv.setText(String.valueOf(slider.getValues().get(1).intValue()));
        });

        binding.seekBarLucidity.addOnChangeListener((slider, value, fromUser) -> {
            binding.seekBarLucidityStartTv.setText(String.valueOf(slider.getValues().get(0).intValue()));
            binding.seekBarLucidityEndTv.setText(String.valueOf(slider.getValues().get(1).intValue()));
        });

        binding.seekBarLucidity.setValues(0.0f, 100.0f);
        binding.seekBarClarity.setValues(0.0f, 100.0f);

        LinearLayout positiveButton = view.findViewById(R.id.btnPositive);
        positiveButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogPositiveClick(
                        binding.seekBarLucidity.getValues().get(0).intValue(),
                        binding.seekBarLucidity.getValues().get(1).intValue(),
                        binding.seekBarClarity.getValues().get(0).intValue(),
                        binding.seekBarClarity.getValues().get(1).intValue(),
                        DreamsHelper.dateStringToDateTime(binding.textViewDateStart.getText().toString()),
                        DreamsHelper.dateStringToDateTime(binding.textViewDateEnd.getText().toString()));
                dismiss();
            }
        });

        LinearLayout negativeButton = view.findViewById(R.id.btnNegative);
        negativeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogNegativeClick();
                dismiss();
            }
        });

        binding.textViewDateStart.setText(DreamsHelper.DateTimeToDateString(LocalDateTime.of(1900, 1, 1, 1, 1)));

        binding.textViewDateEnd.setText(DreamsHelper.DateTimeToDateString(LocalDateTime.now()));

        binding.DateStartLl.setOnClickListener(v -> showDatePickerDialog(binding.textViewDateStart));
        binding.DateEndLl.setOnClickListener(v -> showDatePickerDialog(binding.textViewDateEnd));

        builder.setView(view);

        return builder.create();
    }

    public void setFilterDreamsDialogListener(FilterDreamsDialogListener listener) {
        this.listener = listener;
    }

    private void showDatePickerDialog(TextView textView) {
        // Get current date
        LocalDateTime currentDateTime = LocalDateTime.now();
        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue() - 1; // Adjust for zero-indexed months
        int day = currentDateTime.getDayOfMonth();

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {

                    LocalDateTime selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
                    textView.setText(DreamsHelper.DateTimeToDateString(selectedDateTime));

                },
                year,
                month,
                day
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }

}
