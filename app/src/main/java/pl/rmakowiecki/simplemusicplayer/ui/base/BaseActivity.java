package pl.rmakowiecki.simplemusicplayer.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        bindViews();
        initPresenter();
        presenter.onViewCreated(this);
    }

    private void bindViews() {
        ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        presenter.onViewDestroyed();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (shouldMoveToBack()) {
            super.onBackPressed();
        } else {
            moveTaskToBack(true);
        }
    }

    protected abstract void initPresenter();

    protected abstract int getLayoutResId();

    protected boolean shouldMoveToBack() {
        return true;
    }
}
