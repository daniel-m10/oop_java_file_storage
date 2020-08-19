package com.jalasoft.filestorage.controller.response;

import com.jalasoft.filestorage.model.UploadedFile;

public class FileUploadOkResponse<T> extends Response<T> {
    private UploadedFile uploadedFile;

    public FileUploadOkResponse(final T status, final UploadedFile uploadedFile) {
        super(status);
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadFileResponse() {
        return uploadedFile;
    }

    public void setUploadFileResponse(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}
