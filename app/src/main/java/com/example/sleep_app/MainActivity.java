package com.example.sleep_app;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sleep_app.activities.AddDreamActivity;
import com.example.sleep_app.activities.SettingsActivity;
import com.example.sleep_app.databinding.ActivityMainBinding;
import com.example.sleep_app.mainActivityFragments.DreamsFragment;
import com.example.sleep_app.mainActivityFragments.OverviewFragment;
import com.example.sleep_app.mainActivityFragments.TipsFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TipsFragment tipsFragment;
    OverviewFragment overViewFragment;
    DreamsFragment dreamsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tipsFragment = new TipsFragment();
        overViewFragment = new OverviewFragment();
        dreamsFragment = new DreamsFragment();
        replaceFragment(binding.viewFlipper, 0, dreamsFragment);
        replaceFragment(binding.viewFlipper, 1, overViewFragment);
        replaceFragment(binding.viewFlipper, 2, tipsFragment);

        showOverview();

        binding.btnFragmentA.setOnClickListener(v -> showDreams());

        binding.btnFragmentB.setOnClickListener(v -> showOverview());

        binding.btnFragmentC.setOnClickListener(v -> showTips());

        binding.btnAddDream.setOnClickListener(v -> startActivity(new Intent(this, AddDreamActivity.class)));

        binding.btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        binding.btnFilter.setOnClickListener(v -> dreamsFragment.showFilterDialog());

        binding.btnSearch.setOnClickListener(v -> tipsFragment.openLink());

        binding.btnBack.setOnClickListener(v -> goBackWhenOnTipsAndInfo());

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBackWhenOnTipsAndInfo();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void goBackWhenOnTipsAndInfo(){
        boolean quit = true;
        if (binding.viewFlipper.getDisplayedChild() == 2){ //handler for back press in tipsFragment
            quit = tipsFragment.onBackPressed();
        }
        if (binding.viewFlipper.getDisplayedChild() == 0){ //handler for back press in tipsFragment
            quit = dreamsFragment.onBackPressed();
        }
        if (binding.viewFlipper.getDisplayedChild() == 1) {
            finish();
        } else if (quit) showOverview();
    }

    private void notifyFragmentVisible(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        switch (index) {
            case 0:
                fragment = fragmentManager.findFragmentById(R.id.DreamsFragment); // Replace with the actual ID
                break;
            case 1:
                fragment = fragmentManager.findFragmentById(R.id.OverviewFragment); // Replace with the actual ID
                break;
            case 2:
                fragment = fragmentManager.findFragmentById(R.id.TipsFragment); // Replace with the actual ID
                break;
            default:
                return;
        }
        if (fragment != null) {
            fragment.onResume();
        }
    }


    public void showDreams() {
        binding.viewFlipper.setDisplayedChild(0); // 0 corresponds to the index of FragmentA
        setViewHeight(binding.btnFragmentA, 80);
        setViewHeight(binding.btnFragmentB, 64);
        setViewHeight(binding.btnFragmentC, 64);
        roundUpperEdges(binding.btnFragmentA, new float[]{20, 20, 20, 20, 0, 0, 0, 0}); // todo turn this into a drawable (maybe not)
        roundUpperEdges(binding.btnFragmentB, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        showHideButtons(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE);
        notifyFragmentVisible(0);
    }

    public void showOverview() {
        binding.viewFlipper.setDisplayedChild(1); // 1 corresponds to the index of FragmentB
        setViewHeight(binding.btnFragmentA, 64);
        setViewHeight(binding.btnFragmentB, 80);
        setViewHeight(binding.btnFragmentC, 64);
        roundUpperEdges(binding.btnFragmentA, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        showHideButtons(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        notifyFragmentVisible(1);
    }

    public void showTips() {
        binding.viewFlipper.setDisplayedChild(2); // 2 corresponds to the index of FragmentC
        setViewHeight(binding.btnFragmentA, 64);
        setViewHeight(binding.btnFragmentB, 64);
        setViewHeight(binding.btnFragmentC, 80);
        roundUpperEdges(binding.btnFragmentA, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
        showHideButtons(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE);
        notifyFragmentVisible(2);
    }

    private void replaceFragment(ViewFlipper viewFlipper, int index, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the existing fragment at index with the new one
        transaction.replace(viewFlipper.getChildAt(index).getId(), fragment);

        transaction.commit();
    }

    private void setViewHeight(View view, int heightInDp) {
        // Convert dp to pixels
        Context context = view.getContext();
        int heightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, heightInDp, context.getResources().getDisplayMetrics());

        // Get the existing layout parameters
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

        // Modify the height attribute
        layoutParams.height = heightInPixels;

        // Apply the changes
        view.setLayoutParams(layoutParams);
    }

    private void roundUpperEdges(View view, float[] edges) {
        // Create a drawable with rounded corners
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(ContextCompat.getColor(this, R.color.sleepyBlueDarkest)); // Set your desired color
        gradientDrawable.setCornerRadii(edges); // Adjust the radii as needed

        // Set the drawable as the background of the view
        view.setBackground(gradientDrawable);
    }

    private void showHideButtons(int button1, int button2, int button3, int button4, int button5){
        binding.btnFilter.setVisibility(button1);
        binding.btnAddDream.setVisibility(button2);
        binding.btnSearch.setVisibility(button3);
        binding.btnBack.setVisibility(button4);
        binding.btnSettings.setVisibility(button5);

    }

    public static View makeDreamView(View view, Dream dream){
        // Customize the view based on the data (if needed)
        TextView titleTextView = view.findViewById(R.id.dreamTitleTv);
        titleTextView.setText(dream.getTitle());

        TextView lucidityTextView = view.findViewById(R.id.dreamLucidityTv);
        lucidityTextView.setText(String.valueOf(dream.getLucidity()));

        TextView clarityTextView = view.findViewById(R.id.dreamClarityTv);
        clarityTextView.setText(String.valueOf(dream.getClarity()));

        TextView descriptionTextView = view.findViewById(R.id.dreamDescriptionTv);
        descriptionTextView.setText(dream.getDescription());

        // Calculate the period between the given date and time and the current date and time
        Period period = Period.between(dream.getDateCreated().toLocalDate(), LocalDateTime.now().toLocalDate());

        // Get the number of days and months from the period
        int daysAgo = period.getDays();
        int monthsAgo = period.getMonths();
        int yearsAgo = period.getYears();
        TextView yearTextView = view.findViewById(R.id.dreamDateYearTv);
        if (yearsAgo>2) {
            yearTextView.setText(yearsAgo + " years ago");
        } else if (yearsAgo>0) {
            yearTextView.setText(monthsAgo + " months, " + yearsAgo + " years ago");
        } else if (monthsAgo>6) {
            yearTextView.setText(monthsAgo + " months ago");
        } else if (monthsAgo>0){
            yearTextView.setText(daysAgo + " days, " + monthsAgo + " months ago");
        } else {
            yearTextView.setText(daysAgo + " days ago");
        }

        DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy", Locale.ENGLISH);
        TextView dayTextView = view.findViewById(R.id.dreamDateDayTv);
        dayTextView.setText(dream.getDateCreated().format(customDateFormatter));
        return view;
    }
}