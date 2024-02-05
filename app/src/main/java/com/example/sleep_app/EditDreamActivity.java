package com.example.sleep_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sleep_app.databinding.ActivityEditDreamBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;

public class EditDreamActivity extends AppCompatActivity {

    ActivityEditDreamBinding binding;
    private Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditDreamBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dream = getIntent().getParcelableExtra("EXTRA_DREAM");

        assert dream != null;
        binding.editTextTitle.setText(dream.getTitle());
        binding.seekBarLucidity.setProgress(dream.getLucidity());
        binding.seekBarClarity.setProgress(dream.getClarity());
        binding.editTextFeeling.setText(dream.getFeeling());
        binding.editTextDescription.setText(dream.getDescription());
        binding.seekBarClarityTv.setText(String.valueOf(dream.getClarity()));
        binding.seekBarLucidityTv.setText(String.valueOf(dream.getLucidity()));

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
        binding.buttonEditDream.setOnClickListener(v -> {
            // Retrieve user input
            dream.setTitle(binding.editTextTitle.getText().toString());
            dream.setLucidity(Integer.parseInt(binding.seekBarLucidityTv.getText().toString()));
            dream.setClarity(Integer.parseInt(binding.seekBarClarityTv.getText().toString()));
            dream.setFeeling(binding.editTextFeeling.getText().toString());
            dream.setDescription(binding.editTextDescription.getText().toString());

            if (dream.getTitle().isEmpty()) {
                // Show an error message or handle the empty fields in a way appropriate for your app
                Toast.makeText(getApplicationContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save the dream
                DreamsAccess dreamsAccess = new DreamsAccess(getApplicationContext());
                dreamsAccess.open();
                boolean success = dreamsAccess.editDream(new Dream(dream.getId(), dream.getTitle(), dream.getLucidity(), dream.getClarity(), dream.getFeeling(), dream.getDescription(), dream.getDateCreated()));
                dreamsAccess.close();
                if (success){
                    Log.d("Edit dream","Successfully edited a dream");
                }
                finish();
            }
        });
    }
}