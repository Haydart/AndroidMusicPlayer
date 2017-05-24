package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;

class SongViewHolder extends BaseViewHolder<Song, BaseViewHolder.ListItemListener<Song>> {
    private final View view;
    @BindView(R.id.title_text_view) TextView titleTextView;
    @BindView(R.id.artist_text_view) TextView artistTextView;
    @BindView(R.id.album_cover_image) ImageView albumCoverImageView;

    SongViewHolder(View view, ListItemListener<Song> listener) {
        super(view, listener);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    @Override
    public void bindView(Song song) {
        titleTextView.setText(song.getTitle());
        artistTextView.setText(song.getArtist());
        view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onListItemClicked(song, getAdapterPosition());
            }
        });
        Picasso.with(view.getContext())
                .load(song.getAlbumCoverUri())
                .placeholder(R.drawable.placeholder_album_cover)
                .error(R.drawable.placeholder_album_cover)
                .into(albumCoverImageView);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + artistTextView.getText() + "'";
    }
}
