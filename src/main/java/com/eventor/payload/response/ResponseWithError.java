package com.eventor.payload.response;

public class ResponseWithError<T> {

    private T item;
    private String error;

    public ResponseWithError(T item, String error)
    {
        this.item = item;
        this.error = error;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
