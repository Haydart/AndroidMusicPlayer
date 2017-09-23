package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.DataModel;
import pl.rmakowiecki.simplemusicplayer.model.ListSectionHeader;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseFragment;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

public class AlbumsFragment extends BaseFragment<AlbumsFragmentPresenter> implements AlbumsFragmentView {

    @BindView(R.id.albums_fragment_recycler_view) RecyclerView albumsRecyclerView;

    private static final int COLUMN_COUNT = 2;
    private AlbumCoverClickListener listener;
    private List<DataModel> albumsList;

    public AlbumsFragment() {
    }

    @SuppressWarnings("unused")
    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void setupAlbumsList() {
        albumsList = getArguments().getParcelableArrayList(Constants.EXTRA_ALBUM_MODEL);
        addAlbumArtistHeaders();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return albumsList.get(position) instanceof Album ? 1 : 2;
            }
        });
        albumsRecyclerView.setLayoutManager(gridLayoutManager);
        albumsRecyclerView.setAdapter(new AlbumGridRecyclerViewAdapter(albumsList, new BaseViewHolder.SharedTransitionListItemListener<Album>() {
            @Override
            public void onSharedTransitionListItemClicked(Album modelData, int adapterPosition, View sharedElement) {
                listener.onAlbumClicked(modelData, adapterPosition, sharedElement);
            }

            @Override
            public void onListItemClicked(Album modelData, int adapterPosition) {
                //no-op
            }
        }));
    }

    private void addAlbumArtistHeaders() {
        String previousAlbumArtist = "";
        for (int i = 0; i < albumsList.size(); i++) {
            String currentAlbumArtist = ((Album) albumsList.get(i)).getArtist();
            if (!currentAlbumArtist.equals(previousAlbumArtist)) {
                albumsList.add(i, new ListSectionHeader(currentAlbumArtist));
                previousAlbumArtist = currentAlbumArtist;
            }
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_album_list;
    }

    @Override
    protected void initPresenter() {
        presenter = new AlbumsFragmentPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AlbumCoverClickListener) {
            listener = (AlbumCoverClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SongClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface AlbumCoverClickListener {
        void onAlbumClicked(Album album, int adapterPosition, View albumCoverImageView);
    }
}
