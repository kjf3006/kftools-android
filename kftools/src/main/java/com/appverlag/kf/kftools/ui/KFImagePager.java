package com.appverlag.kf.kftools.ui;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 09.03.17.
 */

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.images.ImageContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Use ImagePager instead.
 */
@Deprecated(forRemoval = true)
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

    public void setImages(List<ImageContainer> images) {
        adapter.setImages(images);
    }



    private class ImagePagerAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;
        private List<ImageContainer> images;

        public ImagePagerAdapter() {
            layoutInflater = (LayoutInflater) KFImagePager.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            images = new ArrayList<>();
        }

        private void setImages(List<ImageContainer> images) {
            this.images = images;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.kftools_pager_item, container, false);

            KFImageView imageView = (KFImageView) itemView.findViewById(R.id.imageView);

            ImageContainer key = images.get(position);
            imageView.setImage(images.get(position));

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
