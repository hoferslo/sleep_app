package com.example.sleep_app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sleep_app.Dream;
import com.example.sleep_app.MainActivity;
import com.example.sleep_app.databinding.ActivityAddDreamBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;
import com.example.sleep_app.sqLiteHelpers.DreamsHelper;

import java.time.LocalDateTime;

public class AddDreamActivity extends AppCompatActivity {

    ActivityAddDreamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDreamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.textViewDate.setText(DreamsHelper.DateTimeToDateTimeString(LocalDateTime.now()));

        binding.seekBarClarity.addOnChangeListener((slider, value, fromUser) -> {
            int progress = (int) value;
            binding.seekBarClarityTv.setText(String.valueOf(progress));
        });

        binding.seekBarLucidity.addOnChangeListener((slider, value, fromUser) -> {
            int progress = (int) value;
            binding.seekBarLucidityTv.setText(String.valueOf(progress));
        });

        binding.seekBarHappiness.addOnChangeListener((slider, value, fromUser) -> {
            int progress = (int) value;
            binding.seekBarHappinessTv.setText(String.valueOf(progress));
            binding.happinessIv.setImageResource(MainActivity.getHappinessImage(progress));
        });

        binding.seekBarHappiness.setValues(50f);

        binding.recurringDreamLl.setOnClickListener(v -> binding.recurringDreamSwitch.callOnClick());

        binding.nightmareLl.setOnClickListener(v -> binding.nightmareSwitch.callOnClick());

        binding.buttonClose.setOnClickListener(v -> finish());

        // Set up click listener for the button
        binding.buttonAddDream.setOnClickListener(v -> {
            // Retrieve user input
            String title = binding.editTextTitle.getText().toString();
            int lucidityStr = Integer.parseInt(binding.seekBarLucidityTv.getText().toString());
            int clarityStr = Integer.parseInt(binding.seekBarClarityTv.getText().toString());
            int happiness = Integer.parseInt(binding.seekBarHappinessTv.getText().toString());
            String description = binding.editTextDescription.getText().toString();
            int recurringDream;
            if (binding.recurringDreamSwitch.isSelected()) recurringDream = 1;
            else recurringDream = 0;
            int nightmare;
            if (binding.nightmareSwitch.isSelected()) nightmare = 1;
            else nightmare = 0;
            LocalDateTime dateTime = DreamsHelper.dateTimeStringToDateTime(binding.textViewDate.getText().toString());
            // Save the dream
            DreamsAccess dreamsAccess = new DreamsAccess(getApplicationContext());
            dreamsAccess.open();
            dreamsAccess.saveDream(new Dream(null, title, lucidityStr, clarityStr, happiness, recurringDream, nightmare, description, dateTime));
            dreamsAccess.close();

            finish();
        });

        binding.imageViewDate.setOnClickListener(v -> showDatePickerDialog());
        binding.textViewDate.setOnClickListener(v -> showDatePickerDialog());
    }


    private void showDatePickerDialog() {
        // Get current date
        LocalDateTime currentDateTime = DreamsHelper.dateTimeStringToDateTime(binding.textViewDate.getText().toString());
        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonthValue() - 1; // Adjust for zero-indexed months
        int day = currentDateTime.getDayOfMonth();

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {

                    LocalDateTime selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 7, 0);
                    binding.textViewDate.setText(DreamsHelper.DateTimeToDateTimeString(selectedDateTime));

                },
                year,
                month,
                day
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }
}