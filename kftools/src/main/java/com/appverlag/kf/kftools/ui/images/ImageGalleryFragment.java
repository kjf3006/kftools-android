package com.appverlag.kf.kftools.ui.images;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.images.KFImageContainer;
import com.appverlag.kf.kftools.other.KFDensityTool;
import com.appverlag.kf.kftools.ui.KFImageView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.List;

public class ImageGalleryFragment extends DialogFragment {

    protected View rootView;
    protected Toolbar toolbar;
    protected ViewPager2 viewPager;

//    protected ImageGalleryViewModel viewModel;

    public List<KFImageContainer> images;
    public int position;

    public ImageGalleryFragment(List<KFImageContainer> images, int position) {
        this.images = images;
        this.position = position;
        setRetainInstance(true);
    }

    public ImageGalleryFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.kftools_image_gallery_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        viewModel = new ViewModelProvider(this).get(ImageGalleryViewModel.class);
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireDialog().onBackPressed());

        viewPager = rootView.findViewById(R.id.viewPager);

        ImageGalleryPagerAdapter adapter = new ImageGalleryPagerAdapter(this);
        adapter.setImages(images);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.setPageTransformer(new MarginPageTransformer(KFDensityTool.dpToPx(30)));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
//        dialog.getWindow().setLayout(width, height);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

//    @Override
//    public void onDestroyView() {
//        Dialog dialog = getDialog();
//        // handles https://code.google.com/p/android/issues/detail?id=17423
//        if (dialog != null && getRetainInstance()) {
//            dialog.setDismissMessage(null);
//        }
//        super.onDestroyView();
//    }


    private static class ImageGalleryPagerAdapter extends FragmentStateAdapter {

        private List<KFImageContainer> images;


        public ImageGalleryPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }


        @SuppressLint("NotifyDataSetChanged")
        public void setImages(List<KFImageContainer> images) {
            this.images = images;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ImageGalleryPageFragment(images.get(position));
        }

        @Override
        public int getItemCount() {
            return images != null ? images.size() : 0;
        }
    }

    public static class ImageGalleryPageFragment extends Fragment {

        private View rootView;
        private KFImageContainer image;
        private PhotoViewAttacher photoViewAttacher;

        public ImageGalleryPageFragment() {

        }

        public ImageGalleryPageFragment(KFImageContainer image) {
            this.image = image;
//            setRetainInstance(true);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.kftools_image_gallery_page_fragment, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            if (image == null) {
                return;
            }
            KFImageView imageView = rootView.findViewById(R.id.imageView);
            imageView.setOnImageChangeListener(v -> {
                if (photoViewAttacher != null) {
                    photoViewAttacher.update();
                }
            });
            imageView.setImage(image);
            photoViewAttacher = new PhotoViewAttacher(imageView);
            photoViewAttacher.setMaximumScale(5);

        }
    }
}
