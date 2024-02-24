package com.appverlag.kf.kftools.ui.formcomponents.controller;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.other.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.Date;

public class KFFormComponentDefaultSerializer implements KFFormComponentSerializer{

    @Override
    public String serialize(@NonNull Object value) {

        if (value instanceof LocalDateTime) { // LocalDateTime to unix timestamp
            return String.valueOf(DateTimeUtils.toEpochSecond((LocalDateTime) value));
        }
        else if (value instanceof Date) { // Date to unix timestamp
            return String.valueOf(((Date) value).getTime() / 1000);
        }
        else if (value instanceof Boolean) { // Boolean to int
            return String.valueOf(((Boolean) value) ? 1 : 0);
        }

        return value.toString();
    }
}
