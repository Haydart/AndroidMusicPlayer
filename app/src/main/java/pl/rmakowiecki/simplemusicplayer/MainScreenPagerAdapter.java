package pl.rmakowiecki.simplemusicplayer;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class MainScreenPagerAdapter extends PagerAdapter {

    private Context mContext;

    public MainScreenPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        MainScreenPagerEnum customPagerEnum = MainScreenPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return MainScreenPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainScreenPagerEnum customPagerEnum = MainScreenPagerEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
