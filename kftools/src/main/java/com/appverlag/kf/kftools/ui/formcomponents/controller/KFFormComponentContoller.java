package com.appverlag.kf.kftools.ui.formcomponents.controller;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;

public class KFFormComponentContoller {

    private List<KFFormComponent<?>> components;

    public KFFormComponentContoller () {
        components = new ArrayList<>();
    }

    public KFFormComponentContoller(@NonNull ViewGroup viewGroup) {
        components = new ArrayList<>();
        addComponentsFromView(viewGroup);
    }

    public void addComponentsFromView(ViewGroup viewGroup) {
        if (viewGroup instanceof KFFormComponent) {
            components.add((KFFormComponent<?>) viewGroup);
        }
        else {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    addComponentsFromView((ViewGroup) child);
                }
            }
        }
    }

    /*
    data management
     */

    @Nullable
    public KFFormComponent<?> getComponent(String identifier) {
        for (KFFormComponent<?> component : components) {
            String key = component.getIdentifier();
            if (key != null && !key.isEmpty() && key.equals(identifier)) {
                return component;
            }
        }
        return null;
    }

    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        for (KFFormComponent<?> component : components) {
            data.put(component.getIdentifier(), component.getValue());
        }
        return data;
    }

    public Map<String, String> getSerializedData() {
        return getSerializedData(new KFFormComponentDefaultSerializer());
    }

    public Map<String, String> getSerializedData(KFFormComponentSerializer serializer) {
        Map<String, String> data = new HashMap<>();
        for (KFFormComponent<?> component : components) {
            Object value = component.getValue();
            String identifier = component.getIdentifier();
            if (identifier != null && value != null) {
                data.put(identifier, serializer.serialize(value));
            }
        }
        return data;
    }

    /**
     * Support for getting okhhtp FormBody from getSerializedData. Uses the KFFormComponentDefaultSerializer as serializer.
     * @return The created FormBody
     */
    public FormBody getSerializedFormBody() {
        return getSerializedFormBody(new KFFormComponentDefaultSerializer());
    }

    /**
     * Support for getting okhhtp FormBody from getSerializedData
     * @param serializer The serializer to be used
     * @return The created FormBody
     */
    public FormBody getSerializedFormBody(KFFormComponentSerializer serializer) {
        Map<String, String> data = getSerializedData(serializer);
        return  buildFormBody(data);
    }

    /**
     * Support for building okhhtp FormBody from Map
     * @param data The data to be put in FormBody
     * @return The created FormBody
     */
    public FormBody buildFormBody(Map<String, String> data) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value != null) {
                formBuilder.add(key, value);
            }
        }
        return  formBuilder.build();
    }

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
