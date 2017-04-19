package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.SongsFragment.SongClickListener;

import java.util.List;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder> {

    private final List<Song> dataSource;
    private final SongClickListener listener;

    public SongRecyclerViewAdapter(List<Song> dataSource, SongClickListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = dataSource.get(position);
        holder.titleTextView.setText(dataSource.get(position).getTitle());
        holder.artistTextView.setText(dataSource.get(position).getArtist());

        holder.view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onSongClicked(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTextView;
        public final TextView artistTextView;
        public Song mItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleTextView = (TextView) view.findViewById(R.id.title_text_view);
            artistTextView = (TextView) view.findViewById(R.id.artist_text_view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + artistTextView.getText() + "'";
        }
    }
}
