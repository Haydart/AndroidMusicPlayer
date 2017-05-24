package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.concurrent.TimeUnit;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

class AlbumViewHolder extends BaseViewHolder<Album, BaseViewHolder.SharedTransitionListItemListener<Album>> {
    private static final int PROGRESS_BAR_VISIBILITY_DELAY = 4000;
    private static final int IGNORED_TARGET_SIZE = 1024;
    private final View view;
    @BindView(R.id.albums_list_item_album_name) TextView albumNameTextView;
    @BindView(R.id.albums_list_item_image_view) ImageView albumCoverImageView;
    @BindView(R.id.albums_list_item_progress_bar) ProgressBar albumCoverImageProgressBar;

    AlbumViewHolder(View view, SharedTransitionListItemListener<Album> listener) {
        super(view, listener);
        ButterKnife.bind(this, view);
        this.view = view;
    }

    @Override
    public void bindView(Album modelData) {
        Picasso.with(view.getContext())
                .load(modelData.getAlbumCoverUri())
                .placeholder(R.drawable.placeholder_album_cover)
                .error(R.drawable.placeholder_album_cover)
                .resize(IGNORED_TARGET_SIZE, IGNORED_TARGET_SIZE)
                .centerInside()
                .into(albumCoverImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        albumCoverImageProgressBar.setVisibility(GONE);
                    }

                    @Override
                    public void onError() {
                        Observable.timer(PROGRESS_BAR_VISIBILITY_DELAY, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(ignored -> albumCoverImageProgressBar.setVisibility(GONE));
                    }
                });

        ViewCompat.setTransitionName(albumCoverImageView, String.valueOf(modelData.getId()));

        albumNameTextView.setText(modelData.getName());
        view.setOnClickListener(v -> {
            if (null != listener) {
                listener.onSharedTransitionListItemClicked(modelData, getAdapterPosition(), albumCoverImageView);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " " + albumNameTextView.getText() + "'";
    }
}
