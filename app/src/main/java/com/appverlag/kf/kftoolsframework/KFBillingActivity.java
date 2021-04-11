package com.appverlag.kf.kftoolsframework;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 15.09.17.
 */
public class KFBillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_overview_activity);



        //setup toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Test");

        //setup recycler view
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        KFBillingAdapter adapter = new KFBillingAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


        /*
    adapter
     */

    private class KFBillingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ViewHolder holderDefault = (ViewHolder) holder;

            //holderDefault.imageView.setImageWithURL(images.get(position), 300, 200, 0);

        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            Button button;
            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.textView);
                button = (Button) view.findViewById(R.id.button);
            }
        }
    }
}
