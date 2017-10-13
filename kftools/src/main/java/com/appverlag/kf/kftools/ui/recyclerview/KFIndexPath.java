package com.appverlag.kf.kftools.ui.recyclerview;


/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 05.10.17.
 */
public class KFIndexPath {

    private int section, position;

    public KFIndexPath() {

    }

    public KFIndexPath(int section, int position) {
        this.section = section;
        this.position = position;
    }

    
    public int getSection() {
        return section;
    }
    
    public void setSection(int section) {
        this.section = section;
    }

    
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
