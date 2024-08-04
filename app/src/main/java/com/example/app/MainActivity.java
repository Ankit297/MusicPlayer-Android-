package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainViewModel viewModel;
    private SongAdapter songAdapter;

    private YouTubePlayerView youTubePlayerView;
    private FloatingActionButton playButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private TextView songTitle;
    private TextView artistName;
    private SeekBar progressBar;
    private TextView currentTime;
    private TextView totalTime;
    private ImageView albumArt;
    private YouTubePlayer activePlayer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initializeViews();
        setupRecyclerView();
        observeSongs();
        setupClickListeners();
    }

    private void initializeViews() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        playButton = findViewById(R.id.playButton);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        songTitle = findViewById(R.id.songTitle);
        artistName = findViewById(R.id.artistName);
        progressBar = findViewById(R.id.progressBar);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        albumArt = findViewById(R.id.albumArt);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.songRecyclerView);
        songAdapter = new SongAdapter(this::onSongClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);
    }

    private void observeSongs() {
        viewModel.getSongs().observe(this, songs -> {
            songAdapter.submitList(songs);
        });
        viewModel.fetchSongs();
    }

    private void onSongClick(Song song) {
        initializeYouTubePlayer(player -> playSong(player, song));
    }

    private void initializeYouTubePlayer(OnYouTubePlayerReadyCallback callback) {
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                activePlayer = youTubePlayer;
                callback.onReady(youTubePlayer);
            }
        }, true);
    }

    private void playSong(YouTubePlayer player, Song song) {
        player.loadVideo(song.getVideoId(), 0);
        updateSongInfo(song.getTitle(), song.getArtist());
        loadAlbumArt(song.getAlbumArtUrl());
        isPlaying = true;
        updatePlayPauseButton();
    }

    private void setupClickListeners() {
        playButton.setOnClickListener(v -> togglePlayPause());
        previousButton.setOnClickListener(v -> playPrevious());
        nextButton.setOnClickListener(v -> playNext());
    }

    private void loadAlbumArt(String url) {
        Log.d(TAG, "Loading album art from URL: " + url);
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_albhum_art)
                .error(R.drawable.default_albhum_art)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(albumArt);
    }

    private void togglePlayPause() {
        if (activePlayer != null) {
            if (isPlaying) {
                activePlayer.pause();
            } else {
                activePlayer.play();
            }
            isPlaying = !isPlaying;
            updatePlayPauseButton();
        }
    }

    private void updatePlayPauseButton() {
        playButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void playPrevious() {
        Song previousSong = viewModel.playPrevious();
        if (previousSong != null && activePlayer != null) {
            playSong(activePlayer, previousSong);
        }
    }

    private void playNext() {
        Song nextSong = viewModel.playNext();
        if (nextSong != null && activePlayer != null) {
            playSong(activePlayer, nextSong);
        }
    }

    private void updateSongInfo(String title, String artist) {
        songTitle.setText(title);
        artistName.setText(artist);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    interface OnYouTubePlayerReadyCallback {
        void onReady(YouTubePlayer player);
    }
}