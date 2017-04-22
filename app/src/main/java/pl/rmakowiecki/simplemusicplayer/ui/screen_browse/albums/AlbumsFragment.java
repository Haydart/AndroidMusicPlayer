package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;

public class AlbumsFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, COLUMN_COUNT));
            List<Album> albumList = getArguments().getParcelableArrayList(ALBUMS);
            recyclerView.setAdapter(new AlbumRecyclerViewAdapter(albumList, listener));
        }
        return view;
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
