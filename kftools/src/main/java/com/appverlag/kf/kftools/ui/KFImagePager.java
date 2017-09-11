package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appverlag.kf.kftools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 09.03.17.
 */
public class KFImagePager extends ViewPager {

    private ImagePagerAdapter adapter;

    public KFImagePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KFImagePager(Context context) {
        super(context);
        init();
    }

    private void init() {
        adapter = new ImagePagerAdapter();
        setAdapter(adapter);
    }

    public void setImages(List<String> images) {
        adapter.setImages(images);
    }



    private class ImagePagerAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;
        private List<String> images;

        public ImagePagerAdapter() {
            layoutInflater = (LayoutInflater) KFImagePager.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            images = new ArrayList<>();
        }

        private void setImages(List<String> images) {
            this.images = images;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.kftools_pager_item, container, false);

            KFImageView imageView = (KFImageView) itemView.findViewById(R.id.imageView);

            String key = images.get(position);
            if (key.contains("http")) {
                imageView.setImageWithURL(key, 0);
            }
            else {
                imageView.setImageWithKey(key, 0);
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
