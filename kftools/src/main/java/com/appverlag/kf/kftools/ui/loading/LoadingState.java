package com.appverlag.kf.kftools.ui.loading;

public enum LoadingState {

    NONE, LOADING, PROGRESS, ERROR;

    private Exception error;
    private long progress;

    public static LoadingState ERROR(String errorMessage) {
        return ERROR(new Exception(errorMessage));
    }

    public static LoadingState ERROR(Exception error) {
        LoadingState loadingState = LoadingState.ERROR;
        loadingState.error = error;
        return loadingState;
    }

    public static LoadingState PROGRESS(long progress) {
        LoadingState loadingState = LoadingState.PROGRESS;
        loadingState.progress = progress;
        return loadingState;
    }

    public Exception getError() {
        return error;
    }

    public long getProgress() {
        return progress;
    }
}
