package com.example.sleep_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

        // Set up click listener for the button
        binding.buttonAddDream.setOnClickListener(v -> {
            // Retrieve user input
            String title = binding.editTextTitle.getText().toString();
            String lucidityStr = binding.editTextLucidity.getText().toString();
            String clarityStr = binding.editTextClarity.getText().toString();
            String feeling = binding.editTextFeeling.getText().toString();
            String description = binding.editTextDescription.getText().toString();

            if (title.isEmpty() || lucidityStr.isEmpty() || clarityStr.isEmpty()) {
                // Show an error message or handle the empty fields in a way appropriate for your app
                Toast.makeText(getApplicationContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Convert lucidity and clarity to integers
                int lucidity = Integer.parseInt(lucidityStr);
                int clarity = Integer.parseInt(clarityStr);

                // Save the dream
                DreamsAccess dreamsAccess = new DreamsAccess(getApplicationContext());
                dreamsAccess.open();
                dreamsAccess.saveDream(new Dream(null, title, lucidity, clarity, feeling, description, LocalDateTime.now()));
                dreamsAccess.close();

                // Optionally, clear the input fields after saving
                binding.editTextTitle.getText().clear();
                binding.editTextLucidity.getText().clear();
                binding.editTextClarity.getText().clear();
                binding.editTextFeeling.getText().clear();
                binding.editTextDescription.getText().clear();
            }
        });
    }
}