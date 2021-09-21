package com.appverlag.kf.kftools.ui.picker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.appverlag.kf.kftools.R;


/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 14.09.17.
 */
public class KFColorPickerDialog extends Dialog {

    private OnColorSelectedListener onColorSelectedListender;

    public KFColorPickerDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kftools_alert_dialog_color_picker);

        KFColorPickerAdapter adapter = new KFColorPickerAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void didPickColor(int color) {
        if (onColorSelectedListender != null) onColorSelectedListender.onColorSelected(this, color);
        dismiss();
    }

    public void setOnColorSelectedListender(OnColorSelectedListener onColorSelectedListender) {
        this.onColorSelectedListender = onColorSelectedListender;
    }

    /*
    adapter
     */

    private class KFColorPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Integer[] items = {0xFFFFCC00, 0xFF34C759, 0xFF5AC8FA, 0xFF007AFF, 0xFF5856D6, 0xFF8F00C9, 0xFFAF52DE, 0xFFFF2D55, 0xFFDF0023, 0xFFFF3B30, 0xFFFF9500 };

        KFColorPickerAdapter() {

        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolderColor(LayoutInflater.from(parent.getContext()).inflate(R.layout.kftools_alert_dialog_color_item, parent, false));
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ViewHolderColor viewHolder = (ViewHolderColor) holder;

            final int color = items[position];
            viewHolder.contentView.setBackgroundColor(color);
            viewHolder.contentView.setOnClickListener(v -> didPickColor(color));

        }

        private class ViewHolderColor extends RecyclerView.ViewHolder {
            View colorIndicator, contentView;

            public ViewHolderColor(View view) {
                super(view);
                contentView = view.findViewById(R.id.contentView);
            }
        }
    }



    public interface OnColorSelectedListener {
        void onColorSelected(KFColorPickerDialog picker, int color);
    }
}
