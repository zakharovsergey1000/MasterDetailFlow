package com.example.sergey.myapplication;

import retrofit2.Call;
import retrofit2.Response;

class HttpRequestResult<T> {
    public Call<T> call;
    public Response<T> response;
    public Throwable error;

    public HttpRequestResult(Call<T> call, Response<T> response, Throwable error) {
        this.call = call;
        this.response = response;
        this.error = error;
    }
}
