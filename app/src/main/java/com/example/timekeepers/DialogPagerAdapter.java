package com.example.timekeepers;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;

public abstract class DialogPagerAdapter extends PagerAdapter {
    private static final String TAG = "DialogPagerAdapter";

    Context context;
    private ViewGroup mViewGroup = null;
    private Dialog mCurrentDialog = null;

    public DialogPagerAdapter(Context context) {
        this.context = context;
    }

    public abstract Dialog getItem(int position);

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mViewGroup == null) {
            mViewGroup = context.
        }

        final long itemId = getItemId(position);
        LayoutInflater inflater = LayoutInflater.from(context);



        switch (position) {
            case 0:

                break;
            case 1:

                break;
        }

        ViewGroup layout = (ViewGroup) inflater.inflate(itemId, container, false);
        container.addView(layout);
        return layout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((DialogFragment) object).getView() == view;
    }

    public long getItemId(int position) {
        return position;
    }
}
