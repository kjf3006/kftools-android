package com.appverlag.kf.kftools.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.images.ImageContainer;
import com.appverlag.kf.kftools.ui.images.ImageGalleryFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class ImagePager extends FrameLayout {

    private final ImageAdapter adapter = new ImageAdapter();
    private TabLayout tabLayout;

    private boolean showsGalleryOnSelection = true;

    private OnImageClickListener onImageClickListener;
    private List<ImageContainer> images;

    public ImagePager(Context context) {
        super(context);
        init();
    }

    public ImagePager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImagePager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ImagePager(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.kftools_image_pager, this);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {

        })).attach();

        adapter.setOnImageClickListener((image, position) -> {
            if (showsGalleryOnSelection) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    ImageGalleryFragment fragment = new ImageGalleryFragment(images, position);
                    fragment.show(activity.getSupportFragmentManager(), null);
                }
            }
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(image, position);
            }
        });
    }

    @Nullable
    private FragmentActivity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public List<ImageContainer> getImages() {
        return images;
    }

    public void setImages(List<ImageContainer> images) {
        this.images = images;
        adapter.setImages(images);
        tabLayout.setVisibility(images.size() > 1 ? VISIBLE : GONE);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public boolean isShowsGalleryOnSelection() {
        return showsGalleryOnSelection;
    }

    public void setShowsGalleryOnSelection(boolean showsGalleryOnSelection) {
        this.showsGalleryOnSelection = showsGalleryOnSelection;
    }

    public interface OnImageClickListener {
        void onImageClick(ImageContainer image, int position);
    }

    private static class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        private OnImageClickListener onImageClickListener;
        private List<ImageContainer> images;

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kftools_image_pager_item, parent, false);
            final ImageViewHolder holder = new ImageViewHolder(view);
            holder.imageView.setOnClickListener(v -> {
                if (onImageClickListener != null) {
                    int position = holder.getBindingAdapterPosition();
                    onImageClickListener.onImageClick(images.get(position), position);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.imageView.setImage(images.get(position));
        }

        @Override
        public int getItemCount() {
            return images != null ? images.size() : 0;
        }

        public List<ImageContainer> getImages() {
            return images;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setImages(List<ImageContainer> images) {
            this.images = images;
            notifyDataSetChanged();
        }

        public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
            this.onImageClickListener = onImageClickListener;
        }
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder {

        public KFImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
