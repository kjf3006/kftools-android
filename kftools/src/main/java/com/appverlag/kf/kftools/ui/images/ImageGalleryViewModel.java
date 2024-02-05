package com.appverlag.kf.kftools.ui.images;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.appverlag.kf.kftools.images.ImageContainer;

import java.util.List;

class ImageGalleryViewModel extends ViewModel {

    public List<ImageContainer> images;
    public int position;

    public ImageGalleryViewModel() {

    }
}
