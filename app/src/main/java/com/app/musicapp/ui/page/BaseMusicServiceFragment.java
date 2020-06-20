package com.app.musicapp.ui.page;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.app.musicapp.service.MusicServiceEventListener;
import com.app.musicapp.ui.BaseActivity;

public abstract class BaseMusicServiceFragment extends Fragment implements MusicServiceEventListener {
    @Override
    public void onServiceConnected() {

    }

    @Override
    public void onServiceDisconnected() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) ((BaseActivity) activity).addMusicServiceEventListener(this);
    }

    @Override
    public void onDestroyView() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) ((BaseActivity) activity).removeMusicServiceEventListener(this);
        super.onDestroyView();
    }

    @Override
    public void onPlayStateChanged() {

    }

    @Override
    public void onRepeatModeChanged() {

    }

    @Override
    public void onShuffleModeChanged() {

    }

    @Override
    public void onQueueChanged() {

    }

    @Override
    public void onPlayingMetaChanged() {

    }

    @Override
    public void onPaletteChanged() {

    }
}
