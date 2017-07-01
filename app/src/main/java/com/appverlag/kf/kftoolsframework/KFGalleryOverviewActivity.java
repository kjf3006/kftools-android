package com.appverlag.kf.kftoolsframework;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appverlag.kf.kftools.ui.KFImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 09.12.16.
 */
public class KFGalleryOverviewActivity extends AppCompatActivity {


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

        int spacingInPixels = Math.round(10 * getResources().getDisplayMetrics().density);

        KFGalleryOverviewAdapter adapter = new KFGalleryOverviewAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels));
        recyclerView.setAdapter(adapter);
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

    private class KFGalleryOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> images;

        public KFGalleryOverviewAdapter() {
            loadData();
        }

        private void loadData() {
            this.images = new ArrayList<>();
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1673.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1672.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1671.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1670.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1669.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1668.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1666.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1665.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1664.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1663.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1662.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1661.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1660.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1659.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1658.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1657.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17505383_10155106444093911_3950715380631971842_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17814220_10155106444368911_6105488263098190799_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17492443_10155106443528911_7379349074121378230_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17758350_10155106444063911_1946269323847113879_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/IMG_1667.JPG");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17492775_10155106444403911_4329663227526809869_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17834807_10155106445578911_6860084080025989915_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17814205_10155106444103911_1931079083400560858_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17761027_10155106446448911_1449403489377908429_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17834218_10155106442943911_2078500801935423617_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17807423_10155106445593911_1973376065199688957_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17834163_10155106441973911_1189311852116449548_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17546682_10155106441978911_4065628480000184267_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17814460_10155106442008911_2763252169036019992_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17545295_10155106442193911_6374700258457873481_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17492541_10155106444373911_7379426504807679335_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17504528_10155106445633911_623050439547503229_o.jpg");
            this.images.add("http://54487993.swh.strato-hosting.eu/jolly/images/gallery/25-03-17/17814260_10155106443513911_8773804337786917073_o.jpg");

            notifyDataSetChanged();
        }



        @Override
        public int getItemCount() {
            return this.images.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_overview_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            ViewHolder holderDefault = (ViewHolder) holder;

            holderDefault.imageView.setImageWithURL(images.get(position), 0);

        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            KFImageView imageView;
            public ViewHolder(View view) {
                super(view);
                imageView = (KFImageView) view.findViewById(R.id.imageView);
                imageView.setAspectRatio(0.7f);
            }
        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;

        public GridSpacingItemDecoration(int spanCount, int spacing) {
            this.spanCount = spanCount;
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        }
    }
}
