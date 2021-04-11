package com.appverlag.kf.kftools.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appverlag.kf.kftools.R;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 09.05.18.
 */
public class KFBottomSheetDialog extends BottomSheetDialogFragment {

    private Button cancelButton;
    private LinearLayout containerView;
    private DialogInterface.OnClickListener onClickListener, cancelOnClickListener;
    private CharSequence [] options;

    public KFBottomSheetDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kftools_bottom_sheet_dialog, container, false);

        cancelButton = view.findViewById(R.id.cancelButton);
        containerView = view.findViewById(R.id.container);

        containerView.setVisibility(options.length > 0 ? View.VISIBLE : View.GONE);
        for (int i = 0; i < options.length; i++) {
            View child = inflater.inflate(R.layout.kftools_bottom_sheet_item, containerView, false);
            ((TextView) child.findViewById(R.id.textView)).setText(options[i]);

            View contentView = child.findViewById(R.id.contentView);
            contentView.setTag(i);
            contentView.setOnClickListener(itemClickListener);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KFBottomSheetDialog.this.dismiss();
                if (cancelOnClickListener != null) cancelOnClickListener.onClick(KFBottomSheetDialog.this.getDialog(), 0);
            }
        });

        return view;
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickListener.onClick(KFBottomSheetDialog.this.getDialog(), (int) v.getTag());
        }
    };

    public DialogInterface.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public DialogInterface.OnClickListener getCancelOnClickListener() {
        return cancelOnClickListener;
    }

    public void setCancelOnClickListener(DialogInterface.OnClickListener cancelOnClickListener) {
        this.cancelOnClickListener = cancelOnClickListener;
    }

    public CharSequence[] getOptions() {
        return options;
    }

    public void setOptions(CharSequence[] options) {
        this.options = options;
    }

    public void setCancelButtonTitle(CharSequence title) {
        this.cancelButton.setText(title);
    }



    public static class Builder {

        private DialogInterface.OnClickListener onClickListener, cancelOnClickListener;
        private CharSequence [] items;
        private CharSequence cancelTitle = "";

        public Builder() {

        }

        public void setItems(CharSequence [] items, DialogInterface.OnClickListener onClickListener) {
            this.items = items;
            this.onClickListener = onClickListener;
        }

        public void setCancelButton(CharSequence title, DialogInterface.OnClickListener onClickListener) {
            this.cancelOnClickListener = onClickListener;
            this.cancelTitle = title;
        }

        public KFBottomSheetDialog create() {
            KFBottomSheetDialog dialog = new KFBottomSheetDialog();
            dialog.setOptions(items);
            dialog.setOnClickListener(onClickListener);
            dialog.setCancelOnClickListener(cancelOnClickListener);
            return dialog;
        }
    }
}
