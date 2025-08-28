package com.appverlag.kf.kftools.other;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.appverlag.kf.kftools.framework.ContextProvider;

import java.util.List;

public class ResUtils {

    private static final int INVALID_RESOURCE_ID = 0;

    private ResUtils() {}

    public static String uriString(@AnyRes int resId) {
        return uri(resId).toString();
    }

    public static String uriString(@NonNull Context context, @AnyRes int resId) {
        return uri(context, resId).toString();
    }

    public static Uri uri(@AnyRes int resId) {
        return uri(ContextProvider.getApplicationContext(), resId);
    }
    public static Uri uri(@NonNull Context context, @AnyRes int resId) {
        Resources resources = context.getResources();
        String uriString = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(resId) + "/"
                + resources.getResourceTypeName(resId) + "/"
                + resources.getResourceEntryName(resId);
//                + resId;
        return Uri.parse(uriString);
    }

    @Nullable
    public static String getString(Context context, String uri) {
        int resId = resId(context, uri);
        if (resId == INVALID_RESOURCE_ID) {
            return null;
        }
        return context.getString(resId);
    }

    @Nullable
    public static String getString(Context context, String uri, Object... formatArgs) {
        int resId = resId(context, uri);
        if (resId == INVALID_RESOURCE_ID) {
            return null;
        }
        return context.getString(resId, formatArgs);
    }

    @Nullable
    public static CharSequence getText(Context context, String uri) {
        int resId = resId(context, uri);
        if (resId == INVALID_RESOURCE_ID) {
            return null;
        }
        return context.getText(resId);
    }

    @Nullable
    public static Bitmap getBitmap(Context context, String uri) {
        int resId = resId(context, uri);
        if (resId == INVALID_RESOURCE_ID) {
            return null;
        }
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    @Nullable
    public static Drawable getDrawable(Context context, String uri) {
        int resId = resId(context, uri);
        if (resId == INVALID_RESOURCE_ID) {
            return null;
        }
        return AppCompatResources.getDrawable(context, resId);
    }

    public static @ColorInt int getColor(Context context, String uri) {
        int resId = resId(context, uri);
        if (resId == INVALID_RESOURCE_ID) {
            return INVALID_RESOURCE_ID;
        }
        return ContextCompat.getColor(context, resId);
    }

    public static int resId(Context context, String uri) {
        if (uri == null) {
            return INVALID_RESOURCE_ID;
        }
        return resId(context, Uri.parse(uri));
    }

    public static int resId(Context context, Uri uri) {
        if (!ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
            return INVALID_RESOURCE_ID;
        }
        List<String> pathSegments = uri.getPathSegments();
        // android.resource//<package_name>/<resource_id>
        if (pathSegments.size() == 1) {
            return resIdFromIdUri(uri);
        }
        // android.resource//<package_name>/<drawable>/<resource_name>
        if (pathSegments.size() == 2) {
            return resIdFromNameUri(context, uri);
        }

        return INVALID_RESOURCE_ID;
    }

    private static int resIdFromNameUri(Context context, Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        String resourceType = pathSegments.get(0);
        String resourceName = pathSegments.get(1);
        @SuppressLint("DiscouragedApi")
        int identifier = context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
        return identifier;
    }

    private static int resIdFromIdUri(Uri uri) {
        try {
            return Integer.parseInt(uri.getPathSegments().get(0));
        }
        catch (Exception e) {
            return INVALID_RESOURCE_ID;
        }
    }
}
