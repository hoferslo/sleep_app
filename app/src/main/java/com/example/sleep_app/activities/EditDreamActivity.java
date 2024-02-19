package com.example.sleep_app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sleep_app.Dream;
import com.example.sleep_app.databinding.ActivityAddDreamBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;
import com.example.sleep_app.sqLiteHelpers.DreamsHelper;

import java.time.LocalDateTime;

public class EditDreamActivity extends AppCompatActivity {

    ActivityAddDreamBinding binding;
    private Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDreamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.buttonAddDreamTv.setText("Save dream");
        dream = getIntent().getParcelableExtra("EXTRA_DREAM");

        assert dream != null;
        binding.editTextTitle.setText(dream.getTitle());
        binding.seekBarLucidity.setValues((float) dream.getLucidity());
        binding.seekBarClarity.setValues((float) dream.getClarity());
        binding.editTextFeeling.setText(dream.getFeeling());
        binding.editTextDescription.setText(dream.getDescription());
        binding.seekBarClarityTv.setText(String.valueOf(dream.getClarity()));
        binding.seekBarLucidityTv.setText(String.valueOf(dream.getLucidity()));
        binding.textViewDate.setText(DreamsHelper.DateTimeToDateTimeString(dream.getDateCreated()));

        binding.seekBarClarity.addOnChangeListener((slider, value, fromUser) -> {

            binding.seekBarClarityTv.setText(String.valueOf((int) value));
        });

        binding.seekBarLucidity.addOnChangeListener((slider, value, fromUser) -> {

            binding.seekBarLucidityTv.setText(String.valueOf((int) value));
        });


        binding.buttonClose.setOnClickListener(v -> finish());

        binding.buttonAddDream.setOnClickListener(v -> {
            // Retrieve user input
            dream.setTitle(binding.editTextTitle.getText().toString());
            dream.setLucidity(Integer.parseInt(binding.seekBarLucidityTv.getText().toString()));
            dream.setClarity(Integer.parseInt(binding.seekBarClarityTv.getText().toString()));
            dream.setFeeling(binding.editTextFeeling.getText().toString());
            dream.setDescription(binding.editTextDescription.getText().toString());
            dream.setDateCreated(DreamsHelper.dateTimeStringToDateTime(binding.textViewDate.getText().toString()));


            // Save the dream
            DreamsAccess dreamsAccess = new DreamsAccess(getApplicationContext());
            dreamsAccess.open();
            boolean success = dreamsAccess.editDream(new Dream(dream.getId(), dream.getTitle(), dream.getLucidity(), dream.getClarity(), dream.getFeeling(), dream.getDescription(), dream.getDateCreated()));
            dreamsAccess.close();
            if (success){
                Log.d("Edit dream","Successfully edited a dream");
            }
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