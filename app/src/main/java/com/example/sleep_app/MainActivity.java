package com.example.sleep_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sleep_app.activities.AddDreamActivity;
import com.example.sleep_app.activities.SettingsActivity;
import com.example.sleep_app.databinding.ActivityMainBinding;
import com.example.sleep_app.databinding.ItemDreamLayoutBinding;
import com.example.sleep_app.mainActivityFragments.DreamsFragment;
import com.example.sleep_app.mainActivityFragments.OverviewFragment;
import com.example.sleep_app.mainActivityFragments.TipsFragment;
import com.example.sleep_app.tools.PrefsHelper;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TipsFragment tipsFragment;
    OverviewFragment overViewFragment;
    DreamsFragment dreamsFragment;

    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        prefsHelper = new PrefsHelper(this);

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
        binding.btnSort.setOnClickListener(v -> dreamsFragment.showSortDialog());

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

    private void goBackWhenOnTipsAndInfo() {
        boolean quit = true;
        if (binding.viewFlipper.getDisplayedChild() == 2) { //handler for back press in tipsFragment
            quit = tipsFragment.onBackPressed();
        }
        if (binding.viewFlipper.getDisplayedChild() == 0) { //handler for back press in tipsFragment
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
        binding.viewFlipper.setDisplayedChild(0);
        setViewHeight(binding.btnFragmentA, 80);
        setViewHeight(binding.btnFragmentB, 64);
        setViewHeight(binding.btnFragmentC, 64);
        roundUpperEdges(binding.btnFragmentA, new float[]{20, 20, 20, 20, 0, 0, 0, 0}); // todo turn this into a drawable (maybe not)
        roundUpperEdges(binding.btnFragmentB, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        showHideButtons(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE);
        notifyFragmentVisible(0);
    }

    public void showOverview() {
        binding.viewFlipper.setDisplayedChild(1);
        setViewHeight(binding.btnFragmentA, 64);
        setViewHeight(binding.btnFragmentB, 80);
        setViewHeight(binding.btnFragmentC, 64);
        roundUpperEdges(binding.btnFragmentA, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        showHideButtons(View.GONE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
        notifyFragmentVisible(1);
    }

    public void showTips() {
        binding.viewFlipper.setDisplayedChild(2);
        setViewHeight(binding.btnFragmentA, 64);
        setViewHeight(binding.btnFragmentB, 64);
        setViewHeight(binding.btnFragmentC, 80);
        roundUpperEdges(binding.btnFragmentA, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
        showHideButtons(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
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

    private void showHideButtons(int button1, int button2, int button3, int button4, int button5, int button6) {
        binding.btnSort.setVisibility(button1);
        binding.btnAddDream.setVisibility(button2);
        binding.btnSearch.setVisibility(button3);
        binding.btnBack.setVisibility(button4);
        binding.btnSettings.setVisibility(button5);
        binding.btnFilter.setVisibility(button6);

    }

    public static View makeDreamView(View view, Dream dream, Context context) {

        // Set dream title
        //ItemDreamLayoutBinding binding = ItemDreamLayoutBinding.inflate(LayoutInflater.from(context), null, false);
        ItemDreamLayoutBinding binding = ItemDreamLayoutBinding.bind(view);

        binding.dreamTitleTv.setText(dream.getTitle());

        // Set dream properties (lucidity, clarity)
        binding.dreamLucidityTv.setText(String.valueOf(dream.getLucidity()));

        {
            ViewGroup.LayoutParams layoutParams1 = binding.dreamLucidityBarLl.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = binding.dreamLucidityBarAntiLl.getLayoutParams();
            ((LinearLayout.LayoutParams) layoutParams1).weight = dream.getLucidity() / 100f;
            ((LinearLayout.LayoutParams) layoutParams2).weight = (100f - dream.getLucidity()) / 100f;
            view.findViewById(R.id.dreamLucidityBarLl).setLayoutParams(layoutParams1);
            view.findViewById(R.id.dreamLucidityBarAntiLl).setLayoutParams(layoutParams2);
        }

        binding.dreamClarityTv.setText(String.valueOf(dream.getClarity()));

        {
            ViewGroup.LayoutParams layoutParams1 = binding.dreamClarityBarLl.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = binding.dreamClarityBarAntiLl.getLayoutParams();
            ((LinearLayout.LayoutParams) layoutParams1).weight = dream.getClarity() / 100f;
            ((LinearLayout.LayoutParams) layoutParams2).weight = (100f - dream.getClarity()) / 100f;
            binding.dreamClarityBarLl.setLayoutParams(layoutParams1);
            binding.dreamClarityBarAntiLl.setLayoutParams(layoutParams2);
        }

        // Set dream description
        binding.dreamDescriptionTv.setText(dream.getDescription());

        // Set formatted date based on relative time period
        binding.dreamDateYearTv.setText(formatRelativeDate(dream.getDateCreated()));

        // Set full date with custom format
        binding.dreamDateDayTv.setText(dream.getDateCreated().format(DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy", Locale.ENGLISH)));

        if (dream.isRecurringDream()) {
            binding.recurringDreamIv.setVisibility(View.VISIBLE);
        }
        if (dream.isNightmare()) {
            binding.nightmareIv.setVisibility(View.VISIBLE);
        }

        binding.happinessIv.setImageResource(getHappinessImage(dream.getHappiness()));
        binding.happinessIv.setPopupText("Happiness of this dream: " + dream.getHappiness() + "/100");

        return binding.getRoot();
    }

    public static int getHappinessImage(int happiness) {
        if (happiness >= 80) {
            return R.mipmap.outline_sentiment_very_satisfied_white_48dp;
        } else if (happiness >= 60) {
            return R.mipmap.outline_sentiment_satisfied_white_48dp;
        } else if (happiness >= 40) {
            return R.mipmap.outline_sentiment_neutral_white_48dp;
        } else if (happiness >= 20) {
            return R.mipmap.outline_sentiment_dissatisfied_white_48dp;
        } else if (happiness >= 0) {
            return R.mipmap.outline_sentiment_very_dissatisfied_white_48dp;
        } else {
            return R.mipmap.ghost; // why not
        }
    }

    private static String formatRelativeDate(LocalDateTime dateCreated) {
        Period period = Period.between(dateCreated.toLocalDate(), LocalDateTime.now().toLocalDate());
        int daysAgo = period.getDays();
        int monthsAgo = period.getMonths();
        int yearsAgo = period.getYears();

        String formattedDate;
        if (yearsAgo > 1) {
            formattedDate = yearsAgo + " years ago";
        } else if (yearsAgo > 0) {
            formattedDate = yearsAgo + " year ago";
        } else if (monthsAgo > 1) {
            formattedDate = monthsAgo + " months ago";
        } else if (monthsAgo > 0) {
            formattedDate = monthsAgo + " month ago";
        } else if (daysAgo > 1) {
            formattedDate = daysAgo + " days ago";
        } else if (daysAgo == 0) {
            formattedDate = "Today";
        } else {
            formattedDate = daysAgo + " day ago";
        }
        return formattedDate;
    }
}