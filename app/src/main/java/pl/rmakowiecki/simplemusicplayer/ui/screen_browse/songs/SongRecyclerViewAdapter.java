package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;

class SongRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<Song> dataSource;
    private final BaseViewHolder.ListItemListener<Song> listener;
    private Context context;

    SongRecyclerViewAdapter(List<Song> dataSource, BaseViewHolder.ListItemListener<Song> listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_all_songs_list_item, parent, false);
        return new SongViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
