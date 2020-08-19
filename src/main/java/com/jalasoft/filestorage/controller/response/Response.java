package com.jalasoft.filestorage.controller.response;

public abstract class Response<T> {
    private T status;

    public Response(T status) {
        this.status = status;
    }

    public T getStatus() {
        return this.status;
    }

    public void setStatus(T status) {
        this.status = status;
    }
}
