package com.example.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("songs")
    Call<List<Song>> getSongs();
}

