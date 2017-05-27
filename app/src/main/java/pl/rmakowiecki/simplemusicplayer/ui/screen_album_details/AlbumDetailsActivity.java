package pl.rmakowiecki.simplemusicplayer.ui.screen_album_details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseActivity;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;
import pl.rmakowiecki.simplemusicplayer.ui.screen_play.MusicPlayActivity;
import pl.rmakowiecki.simplemusicplayer.util.AnimationListenerAdapter;
import pl.rmakowiecki.simplemusicplayer.util.Constants;
import pl.rmakowiecki.simplemusicplayer.util.TransitionListenerAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlbumDetailsActivity extends BaseActivity<AlbumDetailsPresenter> implements AlbumDetailsView, BaseViewHolder.ListItemListener<Song> {

    public static final int ALBUM_COVER_FRAME_ANIMATION_DELAY = 200;
    @BindView(R.id.album_detail_background_image_view) ImageView albumBackgroundImageView;
    @BindView(R.id.album_detail_image_view) ImageView albumImageView;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.album_details_layout) LinearLayout albumDetailsLayout;
    @BindView(R.id.white_frame_background_view) View albumCoverFrame;
    @BindView(R.id.albums_fragment_recycler_view) RecyclerView songListRecyclerView;

    private Album albumDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        setupSharedTransitionStartListener();
        setupHeaderScrollListener();
        setupAppBar();
    }

    @Override
    protected void initPresenter() {
        presenter = new AlbumDetailsPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_album_songs_list;
    }

    private void setupHeaderScrollListener() {
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> presenter.onHeaderOffsetChanged(verticalOffset));
    }

    private void setupSharedTransitionStartListener() {
        Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
        sharedElementEnterTransition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionStart(Transition transition) {
                super.onTransitionStart(transition);
                presenter.onSharedAnimationStarted();
            }
        });
    }

    private void setupAppBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onFloatingActionButtonClicked() {
        presenter.onShufflePlayButtonClicked(albumDataSource.getSongs());
    }

    @Override
    public void fadeOutAlbumDetails() {
        Animation animation = AnimationUtils.loadAnimation(AlbumDetailsActivity.this, R.anim.fade_out);
        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                albumDetailsLayout.setVisibility(View.INVISIBLE);
            }
        });
        albumDetailsLayout.startAnimation(animation);
    }

    @Override
    public void fadeInAlbumDetails() {
        Animation animation = AnimationUtils.loadAnimation(AlbumDetailsActivity.this, R.anim.fade_out);
        animation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                albumDetailsLayout.setVisibility(View.VISIBLE);
            }
        });
        albumDetailsLayout.startAnimation(animation);
    }

    @Override
    public void loadAlbumCoverImage() {
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
    }

    @Override
    public void setupSongsList() {
        songListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songListRecyclerView.setAdapter(new AlbumSongsRecyclerViewAdapter(albumDataSource.getSongs(), this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.onBackButtonClicked();
    }

    @Override
    public void loadHeaderBackground() {
        getAlbumDataFromExtras();
        Picasso.with(this)
                .load(albumDataSource.getAlbumCoverUri())
                .noFade()
                .into(albumBackgroundImageView);
    }

    private void getAlbumDataFromExtras() {
        Bundle extras = getIntent().getExtras();
        albumDataSource = extras.getParcelable(Constants.EXTRA_ALBUM_MODEL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(Constants.EXTRA_ALBUM_IMAGE_TRANSITION_NAME);
            albumImageView.setTransitionName(imageTransitionName);
        }
    }

    @Override
    public void launchMusicPlaybackScreen(List<Song> songDataSource, int position) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putParcelableArrayListExtra(Constants.EXTRA_SONG_MODEL, (ArrayList<? extends Parcelable>) albumDataSource.getSongs());
        startActivity(intent);
    }

    @Override
    public void showHeaderAlbumCoverFrameAnimated() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        animation.setInterpolator(new DecelerateInterpolator());

        Observable.timer(ALBUM_COVER_FRAME_ANIMATION_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                    albumCoverFrame.startAnimation(animation);
                    albumCoverFrame.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void hideHeaderAlbumCoverFrame() {
        albumCoverFrame.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClicked(Song modelData, int adapterPosition) {
        presenter.onListItemClicked(albumDataSource.getSongs(), adapterPosition);
    }
}
