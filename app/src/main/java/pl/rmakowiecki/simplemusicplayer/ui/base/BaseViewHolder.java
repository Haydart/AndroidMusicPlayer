package pl.rmakowiecki.simplemusicplayer.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.rmakowiecki.simplemusicplayer.model.DataModel;

public abstract class BaseViewHolder<M extends DataModel, L extends BaseViewHolder.ListItemListener<M>> extends RecyclerView.ViewHolder {

    protected L listener;

    public BaseViewHolder(View itemView, L listener) {
        super(itemView);
        this.listener = listener;
    }

    public abstract void bindView(M listItemModel);

    public interface ListItemListener<M> {
        void onListItemClicked(M modelData, int adapterPosition);
    }

    public interface SharedTransitionListItemListener<M> extends ListItemListener<M> {
        void onSharedTransitionListItemClicked(M modelData, int adapterPosition, View sharedElement);
    }
}
