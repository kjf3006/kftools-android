package com.appverlag.kf.kftools.other.youtube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.cache.Cache;
import com.appverlag.kf.kftools.network.ConnectionManager;
import com.appverlag.kf.kftools.network.ResponseJSONSerializer;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;

public final class YoutubeVideo {

    private final String youtubeId;

    private Data data;

    public YoutubeVideo(@NonNull String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public static YoutubeVideo fromUrl(@NonNull String url) {
        String youtubeId = extractYoutubeId(url);
        if (youtubeId != null) {
            return new YoutubeVideo(youtubeId);
        }
        return null;
    }

    public static String extractYoutubeId(String url) {
        Pattern pattern = Pattern.compile("((?<=(v|V)/)|(?<=be/)|(?<=(\\?|\\&)v=)|(?<=embed/))([\\w-]++)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public String getYoutubeUrl() {
        return "https://youtu.be/" + youtubeId;
    }

    public String getThumbnailUrl() {
        return "https://i.ytimg.com/vi/" + youtubeId + "/hqdefault.jpg";
    }

    public void getData(@NonNull DataCallback callback) {
        if (data != null) {
            callback.onResult(data);
            return;
        }
        final String cacheKey = "YoutubeVideo.Data." + youtubeId;
        Cache.defaultCache().get(cacheKey, JSONObject.class, object -> {
            if (object != null) {
                data = new Data(object);
                callback.onResult(data);
            }
            else {
                Request request = new Request.Builder()
                        .url("https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=" + youtubeId + "&format=json")
                        .build();
                ConnectionManager.shared().send(request, new ResponseJSONSerializer(), response -> {
                    if (response.success()) {
                        data = new Data(response.value);
                        callback.onResult(data);
                        Cache.defaultCache().put(cacheKey, response.value);
                    }
                    else {
                        callback.onResult(null);
                    }
                });
            }
        });

    }

    public interface DataCallback {
        void onResult(@Nullable Data data);
    }

    public static final class Data {
        private final JSONObject mData;

        public Data(JSONObject data) {
            mData = data;
        }

        public JSONObject getData() {
            return mData;
        }

        public String getTitle() {
            return mData.optString("title");
        }
    }
}
