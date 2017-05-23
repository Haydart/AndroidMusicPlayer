package pl.rmakowiecki.simplemusicplayer.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    private Unbinder unbinder;
    protected P presenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewInit(this);
    }

    @Override
    public void onDestroyView() {
        presenter.onViewDestroy();
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter = null;
        super.onDestroy();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initPresenter();
}