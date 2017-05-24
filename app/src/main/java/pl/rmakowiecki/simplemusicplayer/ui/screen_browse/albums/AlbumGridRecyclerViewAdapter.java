package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.DataModel;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;

class AlbumGridRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final List<DataModel> albumList;
    private final BaseViewHolder.SharedTransitionListItemListener<Album> listener;
    private final static int ALBUM = 0;
    private final static int HEADER = 1;

    AlbumGridRecyclerViewAdapter(List<DataModel> items, BaseViewHolder.SharedTransitionListItemListener<Album> listener) {
        albumList = items;
        this.listener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder viewHolder;

        switch (viewType) {
            case HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_header_list_item, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_album_list_item, parent, false);
                viewHolder = new AlbumViewHolder(view, listener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(albumList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return albumList.get(position) instanceof Album ? ALBUM : HEADER;
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
