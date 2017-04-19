package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;

public class SongsFragment extends Fragment {

    public static final String SONGS = "songs";
    private SongClickListener listener;

    public SongsFragment() {
    }

    @SuppressWarnings("unused")
    public static SongsFragment newInstance() {
        return new SongsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            List<Song> songs = getArguments().getParcelableArrayList(SONGS);
            recyclerView.setAdapter(new SongRecyclerViewAdapter(songs, listener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SongClickListener) {
            listener = (SongClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SongClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface SongClickListener {
        void onSongClicked(Song item);
    }
}
