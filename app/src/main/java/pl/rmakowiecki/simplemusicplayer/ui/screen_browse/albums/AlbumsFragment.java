package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import butterknife.BindView;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseFragment;

public class AlbumsFragment extends BaseFragment<AlbumsFragmentPresenter> implements AlbumsFragmentView {

    @BindView(R.id.albums_fragment_recycler_view) RecyclerView albumsRecyclerView;

    private static final int COLUMN_COUNT = 2;
    public static final String ALBUMS = "albums";
    private AlbumClickListener listener;

    public AlbumsFragment() {
    }

    @SuppressWarnings("unused")
    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void setupAlbumsList() {
        albumsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMN_COUNT));
        List<Album> albumList = getArguments().getParcelableArrayList(ALBUMS);
        albumsRecyclerView.setAdapter(new AlbumRecyclerViewAdapter(albumList, listener));
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
        if (context instanceof AlbumClickListener) {
            listener = (AlbumClickListener) context;
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

    public interface AlbumClickListener {
        void onAlbumClicked(int adapterPosition, Album album, ImageView albumCoverImageView);
    }
}
