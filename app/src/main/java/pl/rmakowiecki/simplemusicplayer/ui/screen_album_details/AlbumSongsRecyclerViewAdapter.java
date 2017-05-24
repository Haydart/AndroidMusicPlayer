package pl.rmakowiecki.simplemusicplayer.ui.screen_album_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs.SongsFragment.SongClickListener;

class AlbumSongsRecyclerViewAdapter extends RecyclerView.Adapter<AlbumSongsRecyclerViewAdapter.AlbumSongViewHolder> {

    private final List<Song> dataSource;
    private final SongClickListener listener;
    private Context context;

    AlbumSongsRecyclerViewAdapter(List<Song> dataSource, SongClickListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    public AlbumSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.album_songs_list_item, parent, false);
        return new AlbumSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumSongViewHolder holder, int position) {
        holder.bindView(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class AlbumSongViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        @BindView(R.id.album_song_title_text_view) TextView titleTextView;
        @BindView(R.id.album_song_duration_text_view) TextView durationTextView;

        AlbumSongViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        void bindView(Song song) {
            titleTextView.setText(song.getTitle());
            durationTextView.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(song.getDurationMillis()),
                    TimeUnit.MILLISECONDS.toSeconds(song.getDurationMillis()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(song.getDurationMillis()))
            ));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + durationTextView.getText() + "'";
        }
    }
}
