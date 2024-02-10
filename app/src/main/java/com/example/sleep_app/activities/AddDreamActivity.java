package com.example.sleep_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.sleep_app.Dream;
import com.example.sleep_app.databinding.ActivityAddDreamBinding;
import com.example.sleep_app.databinding.ActivityMainBinding;
import com.example.sleep_app.sqLiteHelpers.DatabaseHelper;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;

import java.time.LocalDateTime;
import java.util.Date;

public class AddDreamActivity extends AppCompatActivity {

    ActivityAddDreamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDreamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.seekBarClarity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 'progress' is the selected value, ranging from 0 to 100
                // You can use this value as needed
                // For example, display it in a TextView
                binding.seekBarClarityTv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // This method is called when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // This method is called when the user stops touching the SeekBar
            }
        });

        binding.seekBarLucidity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 'progress' is the selected value, ranging from 0 to 100
                // You can use this value as needed
                // For example, display it in a TextView
                binding.seekBarLucidityTv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // This method is called when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // This method is called when the user stops touching the SeekBar
            }
        });

        binding.buttonClose.setOnClickListener(v -> finish());

        // Set up click listener for the button
        binding.buttonAddDream.setOnClickListener(v -> {
            // Retrieve user input
            String title = binding.editTextTitle.getText().toString();
            int lucidityStr = Integer.parseInt(binding.seekBarLucidityTv.getText().toString());
            int clarityStr = Integer.parseInt(binding.seekBarClarityTv.getText().toString());
            String feeling = binding.editTextFeeling.getText().toString();
            String description = binding.editTextDescription.getText().toString();

            // Save the dream
            DreamsAccess dreamsAccess = new DreamsAccess(getApplicationContext());
            dreamsAccess.open();
            dreamsAccess.saveDream(new Dream(null, title, lucidityStr, clarityStr, feeling, description, LocalDateTime.now()));
            dreamsAccess.close();

            finish();
        });
    }
}