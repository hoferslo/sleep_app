package com.example.sleep_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.example.sleep_app.PrefsHelper;
import com.example.sleep_app.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        prefsHelper = new PrefsHelper(this);

        binding.randomDreamSwitch.setChecked(prefsHelper.isRandomDreamEnabled());

        binding.randomDreamSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefsHelper.setRandomDreamEnabled(isChecked);
        });

        binding.buttonClose.setOnClickListener(v -> finish());
    }
}