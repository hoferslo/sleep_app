package com.example.sleep_app.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sleep_app.Dream;
import com.example.sleep_app.activities.EditDreamActivity;
import com.example.sleep_app.databinding.FragmentDreamDetailsBinding;
import com.example.sleep_app.sqLiteHelpers.DreamsAccess;
import com.example.sleep_app.sqLiteHelpers.DreamsHelper;


public class DreamDetailsFragment extends DialogFragment {

    private Dream dream;

    FragmentDreamDetailsBinding binding;

    private boolean isClosedByUser = false;

    public static DreamDetailsFragment newInstance(Dream dreamTemp) {
        DreamDetailsFragment fragment = new DreamDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("dream", dreamTemp);
        fragment.setArguments(args);
        return fragment;
    }

    public DreamDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int theme = android.R.style.ThemeOverlay_Material_Dialog_Alert;
        setStyle(DialogFragment.STYLE_NORMAL, theme);
        if (getArguments() != null) {
            dream = getArguments().getParcelable("dream");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDreamDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (dream != null) {

            binding.TextViewTitle.setText(dream.getTitle());
            
            binding.TextViewLucidity.setText(String.valueOf(dream.getLucidity()));

            {
                ViewGroup.LayoutParams layoutParams1 = binding.LlLucidityBar.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = binding.LlLucidityBarAnti.getLayoutParams();
                ((LinearLayout.LayoutParams) layoutParams1).weight = dream.getLucidity() / 100f;
                ((LinearLayout.LayoutParams) layoutParams2).weight = (100f - dream.getLucidity()) / 100f;
                binding.LlLucidityBar.setLayoutParams(layoutParams1);
                binding.LlLucidityBarAnti.setLayoutParams(layoutParams2);
            }

            binding.TextViewClarity.setText(String.valueOf(dream.getClarity()));

            {
                ViewGroup.LayoutParams layoutParams1 = binding.LlClarityBar.getLayoutParams();
                ViewGroup.LayoutParams layoutParams2 = binding.LlClarityBarAnti.getLayoutParams();
                ((LinearLayout.LayoutParams) layoutParams1).weight = dream.getClarity() / 100f;
                ((LinearLayout.LayoutParams) layoutParams2).weight = (100f - dream.getClarity()) / 100f;
                binding.LlClarityBar.setLayoutParams(layoutParams1);
                binding.LlClarityBarAnti.setLayoutParams(layoutParams2);
            }
            binding.TextViewDescription.setText(dream.getDescription());
            binding.TextViewDateTime.setText(DreamsHelper.DateTimeToDateTimeString(dream.getDateCreated()));
        }

        binding.buttonClose.setOnClickListener(v -> closeFragment());

        binding.buttonEditDream.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EditDreamActivity.class);
            intent.putExtra("EXTRA_DREAM", dream);
            startActivity(intent);
            closeFragment();
        });

        binding.buttonDeleteDream.setOnClickListener(v -> {
            DeleteDreamDialogFragment deleteDreamDialog = new DeleteDreamDialogFragment();
            deleteDreamDialog.setDeleteDreamDialogListener(new DeleteDreamDialogFragment.DeleteDreamDialogListener() {
                @Override
                public void onDialogPositiveClick() {
                    DreamsAccess dreamsAccess = new DreamsAccess(requireContext());
                    dreamsAccess.open();
                    boolean success = dreamsAccess.deleteDream(dream.getId());
                    dreamsAccess.close();
                    if (success) {
                        Log.d("Delete dream", "Successfully deleted a dream");
                    } else {
                        Log.d("Delete dream error", "Failed to delete a dream");
                    }
                    closeFragment();
                }

                @Override
                public void onDialogNegativeClick() {

                }
            });
            deleteDreamDialog.show(getParentFragmentManager(), "deleteDreamDialog");

        });
    }

    private void closeFragment() {
        isClosedByUser = true;

        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Begin the FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remove the current fragment from the container
        transaction.remove(this);

        // Commit the transaction
        transaction.commit();
    }

    public interface OnDialogDismissListener {
        void onDialogDismissed();
    }

    private OnDialogDismissListener onDialogDismissListener;

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // Notify the callback listener that the dialog is dismissed
        if (onDialogDismissListener != null && !isClosedByUser) {
            onDialogDismissListener.onDialogDismissed();
        }
    }

    public void setOnDialogDismissListener(OnDialogDismissListener listener) {
        this.onDialogDismissListener = listener;
    }
}
