package com.example.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class SongAdapter extends ListAdapter<Song, SongAdapter.SongViewHolder> {

    private final OnSongClickListener listener;

    protected SongAdapter(OnSongClickListener listener) {
        super(new SongDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView artistTextView;
        private final ImageView songImageView;
        private final OnSongClickListener listener;

        SongViewHolder(@NonNull View itemView, OnSongClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songTitle);
            artistTextView = itemView.findViewById(R.id.artistName);
            songImageView=itemView.findViewById(R.id.songImage);
            this.listener = listener;
        }

        void bind(Song song) {
            titleTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist());
            Glide.with(itemView.getContext())
                    .load(song.getAlbumArtUrl())
                    .placeholder(R.drawable.default_albhum_art)
                    .error(R.drawable.default_albhum_art)
                    .into(songImageView);

            itemView.setOnClickListener(v -> listener.onSongClick(song));
        }
    }

    public interface OnSongClickListener {
        void onSongClick(Song song);
    }

    static class SongDiffCallback extends DiffUtil.ItemCallback<Song> {
        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.getVideoId().equals(newItem.getVideoId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
          return oldItem.equals(newItem);
        }
    }
}