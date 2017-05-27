package pl.rmakowiecki.simplemusicplayer.ui.screen_album_details;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;

class AlbumSongViewHolder extends BaseViewHolder<Song, BaseViewHolder.ListItemListener<Song>> {
    private final View view;
    @BindView(R.id.album_song_title_text_view) TextView titleTextView;
    @BindView(R.id.album_song_duration_text_view) TextView durationTextView;

    AlbumSongViewHolder(View view, ListItemListener<Song> listener) {
        super(view, listener);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    public void bindView(Song song) {
        titleTextView.setText(song.getTitle());
        durationTextView.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(song.getDurationMillis()),
                TimeUnit.MILLISECONDS.toSeconds(song.getDurationMillis()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(song.getDurationMillis()))
        ));
        view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onListItemClicked(song, getAdapterPosition());
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + durationTextView.getText() + "'";
    }
}