package com.appverlag.kf.kftools.ui.formcomponents.controller;

import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KFFormComponentContoller {

    private List<KFFormComponent<?>> components;

    public KFFormComponentContoller () {
        components = new ArrayList<>();
    }

    /*
    data management
     */


    /*
    getter & setter
     */

    public void addComponent(KFFormComponent<?> component) {
        components.add(component);
    }

    public void addComponents(KFFormComponent<?> ... components) {
        this.components.addAll(Arrays.asList(components));
    }

    public List<KFFormComponent<?>> getComponents() {
        return components;
    }

    public void setComponents(List<KFFormComponent<?>> components) {
        this.components = components;
    }
}
