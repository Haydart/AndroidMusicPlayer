package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.SongsFragment.SongClickListener;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.SongViewHolder> {

    private final List<Song> dataSource;
    private final SongClickListener listener;
    private Context context;

    public SongRecyclerViewAdapter(List<Song> dataSource, SongClickListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_song_list_row, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        holder.bindView(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        @BindView(R.id.title_text_view) TextView titleTextView;
        @BindView(R.id.artist_text_view) TextView artistTextView;
        @BindView(R.id.album_cover_image) ImageView albumCoverImageView;

        SongViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        void bindView(Song song) {
            titleTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist());
            view.setOnClickListener(v -> {
                if (null != listener) {
                    listener.onSongClicked(song);
                }
            });
            Picasso.with(context)
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
}
