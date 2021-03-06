package com.app.musicapp.ui.page.librarypage.genre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.app.musicapp.R;
import com.app.musicapp.ui.page.BaseMusicServiceFragment;

public class GenreChildTab extends BaseMusicServiceFragment {
    public static final String TAG = "GenreChildTab";

    public static GenreChildTab newInstance() {
        return new GenreChildTab();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.genre_child_tab, container, false);
    }

    @BindDimen(R.dimen.minimum_bottom_back_stack_margin)
    float mMinBottomPadding;

    @BindDimen(R.dimen._16dp)
    float m16Dp;

    private Unbinder mUnbinder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onMediaStoreChanged() {
    }

    @Override
    public void onDestroyView() {

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        super.onDestroyView();
    }
}

