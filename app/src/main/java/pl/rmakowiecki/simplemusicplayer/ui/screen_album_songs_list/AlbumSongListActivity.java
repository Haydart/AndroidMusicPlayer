package pl.rmakowiecki.simplemusicplayer.ui.screen_album_songs_list;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import pl.rmakowiecki.simplemusicplayer.Constants;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;

public class AlbumSongListActivity extends AppCompatActivity {

    @BindView(R.id.album_detail_background_image_view) ImageView albumBackgroundImageView;
    @BindView(R.id.album_detail_image_view) ImageView albumImageView;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Album albumDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        albumDataSource = extras.getParcelable(Constants.EXTRA_ALBUM_MODEL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(Constants.EXTRA_ALBUM_IMAGE_TRANSITION_NAME);
            albumImageView.setTransitionName(imageTransitionName);
        }
        Picasso.with(this)
                .load(albumDataSource.getAlbumCoverUri())
                .noFade()
                .into(albumBackgroundImageView);

        Picasso.with(this)
                .load(albumDataSource.getAlbumCoverUri())
                .noFade()
                .into(albumImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });

        floatingActionButton.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }
}
