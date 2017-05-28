package pl.rmakowiecki.simplemusicplayer.ui.base;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BasePresenter<V extends BaseView> {
    protected V view;

    protected void onViewInit(V view) {
        this.view = view;
    }

    protected void onViewDestroy() {
        view = null;
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}