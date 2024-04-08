package com.example.sleep_app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sleep_app.databinding.CustomSortDreamsDialogBinding;
import com.example.sleep_app.enums.SortOption;

public class SortDreamsDialogFragment extends DialogFragment {

    CustomSortDreamsDialogBinding binding;
    SortOption sortOption;
    boolean sortOrder = false;

    public interface SortDreamsDialogListener {
        void onDialogPositiveClick(SortOption sortOption, boolean sortOrder); //sortOrder false = descending, true = ascending

        void onDialogNegativeClick();
    }

    private SortDreamsDialogListener listener;

    public static SortDreamsDialogFragment newInstance(SortOption sortOption, boolean sortOrder) {
        SortDreamsDialogFragment sortDreamsDialog = new SortDreamsDialogFragment();
        sortDreamsDialog.sortOption = sortOption;
        sortDreamsDialog.sortOrder = sortOrder;
        return sortDreamsDialog;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        binding = CustomSortDreamsDialogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        if (sortOption == null) {
            sortOption = SortOption.DATE;
        }

        setButtonsState(sortOption);
        setSortOrderIv(sortOrder);

        binding.clarity.setOnClickListener((View.OnClickListener) v -> {
            sortOption = SortOption.CLARITY;
            setButtonsState(sortOption);
        });
        binding.lucidity.setOnClickListener((View.OnClickListener) v -> {
            sortOption = SortOption.LUCIDITY;
            setButtonsState(sortOption);
        });
        binding.date.setOnClickListener((View.OnClickListener) v -> {
            sortOption = SortOption.DATE;
            setButtonsState(sortOption);
        });
        binding.alphabetically.setOnClickListener((View.OnClickListener) v -> {
            sortOption = SortOption.ALPHABETICALLY;
            setButtonsState(sortOption);
        });


        binding.btnPositive.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogPositiveClick(sortOption, sortOrder);
                dismiss();
            }
        });

        binding.btnNegative.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogNegativeClick();
                dismiss();
            }
        });

        binding.btnNegative.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDialogNegativeClick();
                dismiss();
            }
        });

        binding.sortOrderLl.setOnClickListener(v -> {
            sortOrder = !sortOrder;
            setSortOrderIv(sortOrder);
        });

        builder.setView(view);

        return builder.create();
    }

    public void setSortOrderIv(boolean sortOrder) {
        if (sortOrder) {
            binding.imageViewSortArrow.setRotation(90);
            binding.imageViewSort.setRotation(180);
        } else {
            binding.imageViewSortArrow.setRotation(270);
            binding.imageViewSort.setRotation(0);
        }
    }

    public void setSortDreamsDialogListener(SortDreamsDialogListener listener) {
        this.listener = listener;
    }

    public void setButtonsState(SortOption sortOption) {
        int notSelectedColor = Color.GRAY;
        int selectedColor = Color.WHITE;

        binding.clarityTv.setTextColor(notSelectedColor);
        binding.clarity.setPressed(true);

        binding.lucidityTv.setTextColor(notSelectedColor);
        binding.lucidity.setPressed(true);

        binding.dateTv.setTextColor(notSelectedColor);
        binding.date.setPressed(true);

        binding.alphabeticallyTv.setTextColor(notSelectedColor);
        binding.alphabetically.setPressed(true);

        if (sortOption.equals(SortOption.CLARITY)) {
            binding.clarityTv.setTextColor(selectedColor);
            binding.clarity.setPressed(false);
        } else if (sortOption.equals(SortOption.LUCIDITY)) {
            binding.lucidityTv.setTextColor(selectedColor);
            binding.lucidity.setPressed(false);
        } else if (sortOption.equals(SortOption.DATE)) {
            binding.dateTv.setTextColor(selectedColor);
            binding.date.setPressed(false);
        } else if (sortOption.equals(SortOption.ALPHABETICALLY)) {
            binding.alphabeticallyTv.setTextColor(selectedColor);
            binding.alphabetically.setPressed(false);
        }
    }
}

