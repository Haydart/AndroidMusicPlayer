package pl.rmakowiecki.simplemusicplayer.ui.screen_album_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;

class AlbumSongsRecyclerViewAdapter extends RecyclerView.Adapter<AlbumSongViewHolder> {

    private final List<Song> dataSource;
    private final BaseViewHolder.ListItemListener<Song> listener;
    private Context context;

    AlbumSongsRecyclerViewAdapter(List<Song> dataSource, BaseViewHolder.ListItemListener<Song> listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    public AlbumSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.album_songs_list_item, parent, false);
        return new AlbumSongViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(final AlbumSongViewHolder holder, int position) {
        holder.bindView(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
