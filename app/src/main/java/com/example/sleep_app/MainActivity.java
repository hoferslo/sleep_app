package com.example.sleep_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sleep_app.databinding.ActivityMainBinding;
import com.example.sleep_app.mainActivityFragments.DreamsFragment;
import com.example.sleep_app.mainActivityFragments.OverviewFragment;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        replaceFragment(binding.viewFlipper, 0, new DreamsFragment());
        replaceFragment(binding.viewFlipper, 1, new OverviewFragment());
        replaceFragment(binding.viewFlipper, 2, new Fragment());

        showOverview();

        binding.btnFragmentA.setOnClickListener(v -> showFragmentA());

        binding.btnFragmentB.setOnClickListener(v -> showOverview());

        binding.btnFragmentC.setOnClickListener(v -> showFragmentC());
    }

    public void showFragmentA() {
        binding.viewFlipper.setDisplayedChild(0); // 0 corresponds to the index of FragmentA
        setViewHeight(binding.btnFragmentA, 80);
        setViewHeight(binding.btnFragmentB, 64);
        setViewHeight(binding.btnFragmentC, 64);
        roundUpperEdges(binding.btnFragmentA, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
    }

    public void showOverview() {
        binding.viewFlipper.setDisplayedChild(1); // 1 corresponds to the index of FragmentB
        setViewHeight(binding.btnFragmentA, 64);
        setViewHeight(binding.btnFragmentB, 80);
        setViewHeight(binding.btnFragmentC, 64);
        roundUpperEdges(binding.btnFragmentA, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
    }

    public void showFragmentC() {
        binding.viewFlipper.setDisplayedChild(2); // 2 corresponds to the index of FragmentC
        setViewHeight(binding.btnFragmentA, 64);
        setViewHeight(binding.btnFragmentB, 64);
        setViewHeight(binding.btnFragmentC, 80);
        roundUpperEdges(binding.btnFragmentA, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentB, new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        roundUpperEdges(binding.btnFragmentC, new float[]{20, 20, 20, 20, 0, 0, 0, 0});
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
        gradientDrawable.setColor(getResources().getColor(R.color.sleepyBlue)); // Set your desired color
        gradientDrawable.setCornerRadii(edges); // Adjust the radii as needed

        // Set the drawable as the background of the view
        view.setBackground(gradientDrawable);
    }
}