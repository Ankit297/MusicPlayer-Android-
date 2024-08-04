package com.example.app;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Song>> songs = new MutableLiveData<>();
    private int currentSongIndex = 0;

    public LiveData<List<Song>> getSongs() {
        return songs;
    }

    public void fetchSongs() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Song>> call = apiService.getSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    songs.setValue(response.body());
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public Song playPrevious() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            return songs.getValue().get(currentSongIndex);
        }
        return null;
    }

    public Song playNext() {
        if (currentSongIndex < songs.getValue().size() - 1) {
            currentSongIndex++;
            return songs.getValue().get(currentSongIndex);
        }
        return null;
    }
}