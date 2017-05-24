package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.DataModel;
import pl.rmakowiecki.simplemusicplayer.model.ListSectionHeader;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseViewHolder;

class HeaderViewHolder extends BaseViewHolder {

    @BindView(R.id.header_albums_artist_text_view) TextView artistTextView;

    public HeaderViewHolder(View view) {
        super(view, (modelData, adapterPosition) -> {/*no-op*/});
        ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(DataModel modelData) {
        artistTextView.setText(((ListSectionHeader) modelData).getHeaderext());
    }
}
