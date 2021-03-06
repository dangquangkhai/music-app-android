package com.app.musicapp.ui.page.subpages;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.app.musicapp.R;
import com.app.musicapp.helper.menu.SongMenuHelper;
import com.app.musicapp.ui.bottomsheet.OptionBottomSheet;
import com.app.musicapp.ui.page.librarypage.song.SongChildAdapter;

public class SongInArtistPagerAdapter extends SongChildAdapter {
    private static final String TAG = "SongInArtistPagerAdapter";

    public SongInArtistPagerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return R.layout.item_sort_song_child;
        return R.layout.item_song_bigger;
    }

    @Override
    protected void onMenuItemClick(int positionInData) {
        OptionBottomSheet
                .newInstance(SongMenuHelper.SONG_ARTIST_OPTION, getData().get(positionInData))
                .show(((AppCompatActivity) mContext).getSupportFragmentManager(), "song_popup_menu");
    }
}
