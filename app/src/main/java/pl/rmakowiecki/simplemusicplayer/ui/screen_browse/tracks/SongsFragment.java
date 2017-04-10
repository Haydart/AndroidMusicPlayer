package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.dummy.DummyContent;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.dummy.DummyContent.DummyItem;

public class SongsFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;

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
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new SongRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onSongsListInteraction(DummyItem item);
    }
}
