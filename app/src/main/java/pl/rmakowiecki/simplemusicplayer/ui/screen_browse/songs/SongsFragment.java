package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseFragment;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

public class SongsFragment extends BaseFragment<SongsFragmentPresenter> implements SongsFragmentView {

    @BindView(R.id.songs_fragment_recycler_view) RecyclerView songsRecyclerView;

    private SongClickListener listener;

    public SongsFragment() {
    }

    @SuppressWarnings("unused")
    public static SongsFragment newInstance() {
        return new SongsFragment();
    }

    @Override
    public void setupSongsList() {
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Song> songsList = getArguments().getParcelableArrayList(Constants.EXTRA_SONG_MODEL);
        songsRecyclerView.setAdapter(new SongRecyclerViewAdapter(songsList, listener));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_song_list;
    }

    @Override
    protected void initPresenter() {
        presenter = new SongsFragmentPresenter();
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
        void onSongClicked(Song adapterPosition, int item);
    }
}
