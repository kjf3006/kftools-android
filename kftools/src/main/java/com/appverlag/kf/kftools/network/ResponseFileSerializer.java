package com.appverlag.kf.kftools.network;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseFileSerializer extends ResponseSerializer<File> {

    private final File file;

    public ResponseFileSerializer(@NonNull Context context) {
        file = context.getCacheDir();
    }

    /**
     * Create a serializer with specified file
     * @param file The file to be saved to.
     */
    public ResponseFileSerializer(@NonNull File file) {
        this.file = file;
    }

    @Override
    public File serialize(okhttp3.Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw NetworkException.noDataReceived();
        }

        File file = getFileForResponse(response);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(body.bytes());
        fos.close();
        return file;
    }

    private File getFileForResponse(Response response) throws Exception {
        if (!file.isDirectory()) {
            return file;
        }

        ResponseBody responseBody = response.body();

        String fileExtension = "";
        MediaType mediaType = responseBody != null ? responseBody.contentType() : null;
        if (mediaType != null) {
            fileExtension = "." + mediaType.subtype();
        }
        String fileName = response.request().hashCode() + fileExtension;

        if (file.exists() || file.mkdirs()) {
            return new File(file.getAbsolutePath() + "/" + fileName);
        }
        else {
            throw new NetworkException("Unable to create folder at: " + file.getAbsolutePath());
        }
    }
}
