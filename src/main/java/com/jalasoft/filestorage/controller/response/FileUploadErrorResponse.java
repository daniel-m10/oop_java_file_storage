package com.jalasoft.filestorage.controller.response;

public class FileUploadErrorResponse<T> extends Response<T> {
    private String errorMessage;

    public FileUploadErrorResponse(final T status, final String errorMessage) {
        super(status);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
