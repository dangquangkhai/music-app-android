package com.app.musicapp.ui.playingqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.app.musicapp.R;
import com.app.musicapp.contract.AbsBindAbleHolder;
import com.app.musicapp.contract.AbsSongAdapter;
import com.app.musicapp.helper.menu.SongMenuHelper;
import com.app.musicapp.ui.bottomsheet.OptionBottomSheet;
import org.jetbrains.annotations.NotNull;

public class PlayingQueueAdapter extends AbsSongAdapter {
    private static final String TAG = "PlayingQueueAdapter";

    public PlayingQueueAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onMenuItemClick(int positionInData) {
        OptionBottomSheet
                .newInstance(SongMenuHelper.SONG_QUEUE_OPTION, getData().get(positionInData))
                .show(((AppCompatActivity) mContext).getSupportFragmentManager(), "song_popup_menu");
    }

    @NotNull
    @Override
    public AbsBindAbleHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song_big, viewGroup, false);
        return new AbsSongAdapter.SongHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsBindAbleHolder absBindAbleHolder, int i) {
        if (absBindAbleHolder instanceof AbsSongAdapter.SongHolder)
            absBindAbleHolder.bind(getData().get(getDataPosition(i)));
    }
}
