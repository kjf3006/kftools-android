package com.appverlag.kf.kftoolsframework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appverlag.kf.kftools.images.KFImageManager;
import com.appverlag.kf.kftools.ui.recyclerview.KFIndexPath;
import com.appverlag.kf.kftools.ui.recyclerview.KFRecyclerViewAdapter;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 04.10.17.
 */
public class KFSectionedRecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_recyclerview);


        KFImageManager.getInstance(getApplicationContext()).reset();

        //setup toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Test");

        //setup recycler view
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


//        KFSectionedAdapter adapter = new KFSectionedAdapter();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new StickyHeaderGridLayoutManager(1));
//        recyclerView.setAdapter(adapter);
//        recyclerView.scrollToPosition(20);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


        /*
    adapter
     */

//    private class KFSectionedAdapter extends StickyHeaderGridAdapter {
//
//
//        @Override
//        public int getSectionCount() {
//            return 3;
//        }
//
//        @Override
//        public int getSectionItemCount(int section) {
//            return 10;
//        }
//
//        @Override
//        public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
//            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_section_header, parent, false);
//            return new _ViewHolderHeader(view);
//        }
//
//        @Override
//        public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
//            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_section_item, parent, false);
//            return new _ViewHolderItem(view);
//        }
//
//
//        @Override
//        public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, final int section) {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("TEST", "header pressed: " + section);
//                }
//            });
//        }
//
//
//        @Override
//        public void onBindItemViewHolder(ItemViewHolder viewHolder, final int section, final int position) {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("TEST", "item pressed: " + section + ", " + position);
//                }
//            });
//        }
//
//        public class _ViewHolderHeader extends HeaderViewHolder {
//            public _ViewHolderHeader(View view) {
//                super(view);
//            }
//        }
//
//        public class _ViewHolderItem extends ItemViewHolder {
//            public _ViewHolderItem(View view) {
//                super(view);
//            }
//        }
//    }
}
