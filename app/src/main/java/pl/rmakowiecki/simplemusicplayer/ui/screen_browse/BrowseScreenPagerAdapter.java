package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.AlbumsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs.SongsFragment;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

class BrowseScreenPagerAdapter extends FragmentPagerAdapter {
    private static final String PAGE_POSITION = "page_position";
    private Context context;
    private List<Song> songList;
    private final List<Album> albumList;

    BrowseScreenPagerAdapter(FragmentManager fragmentManager, Context context, List<Song> songList, List<Album> albumList) {
        super(fragmentManager);
        this.context = context;
        this.songList = songList;
        this.albumList = albumList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result;
        Bundle args = new Bundle();
        args.putInt(PAGE_POSITION, position + 1);

        if (position == 0) {
            result = SongsFragment.newInstance();
            args.putParcelableArrayList(Constants.EXTRA_SONG_MODEL, (ArrayList<Song>) songList);
        } else {
            result = AlbumsFragment.newInstance();
            args.putParcelableArrayList(Constants.EXTRA_ALBUM_MODEL, (ArrayList<Album>) albumList);
        }
        result.setArguments(args);
        return result;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final int stringId;
        if (position == 0) {
            stringId = R.string.tracks;
        } else {
            stringId = R.string.albums;
        }
        return context.getString(stringId);
    }
}
