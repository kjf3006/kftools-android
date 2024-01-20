package com.appverlag.kf.kftools.network;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.framework.ContextProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseFileSerializer extends ResponseSerializer<File> {

    private final File file;

    public ResponseFileSerializer() {
        file = ContextProvider.getApplicationContext().getCacheDir();
    }

    /**
     * Create a serializer with specified file or folder
     * @param file The file or folder to be saved to.
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
        if (body.contentLength() == 0) {
            body.close();
            throw new NetworkException("Received response with 0 content-length header.");
        }

        File file = getFileForResponse(response);

        // return existing file, if cache hit
        if (file.exists() && response.cacheResponse() != null) {
            return file;
        }

        InputStream inputStream = new BufferedInputStream(body.byteStream());
        OutputStream outputStream = new FileOutputStream(file, false);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        body.close();

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
        String fileName = response.request().toString().hashCode() + fileExtension;

        if (file.exists() || file.mkdirs()) {
            return new File(file.getAbsolutePath() + "/" + fileName);
        }
        else {
            throw new NetworkException("Unable to create folder at: " + file.getAbsolutePath());
        }
    }
}
