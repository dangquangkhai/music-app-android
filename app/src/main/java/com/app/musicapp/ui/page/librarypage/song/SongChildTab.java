package com.app.musicapp.ui.page.librarypage.song;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.app.musicapp.App;
import com.app.musicapp.R;
import com.app.musicapp.contract.AbsMediaAdapter;
import com.app.musicapp.loader.medialoader.SongLoader;
import com.app.musicapp.model.Song;
import com.app.musicapp.ui.bottomsheet.SortOrderBottomSheet;
import com.app.musicapp.ui.page.BaseMusicServiceFragment;
import com.app.musicapp.util.Animation;
import com.app.musicapp.util.Tool;
import com.app.musicapp.util.Util;
import com.bumptech.glide.Glide;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

public class SongChildTab extends BaseMusicServiceFragment implements SortOrderBottomSheet.SortOrderChangedListener, PreviewRandomPlayAdapter.FirstItemCallBack {
    public static final String TAG = "SongChildTab";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

//    @BindView(R.id.preview_shuffle_list)
//    RecyclerView mPreviewRecyclerView;

    @BindView(R.id.refresh)
    ImageView mRefresh;

    @BindView(R.id.image)
    ImageView mImage;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.description)
    TextView mArtist;

    @BindView(R.id.random_group)
    Group mRandomGroup;

    private int mCurrentSortOrder = 0;

    private void initSortOrder() {
        mCurrentSortOrder = App.getInstance().getPreferencesUtility().getSongChildSortOrder();
    }

//    @BindView(R.id.top_background) View mTopBackground;
//    @BindView(R.id.bottom_background) View mBottomBackground;
//    @BindView(R.id.random_header) View mRandomHeader;
//    @BindView(R.id.shuffle_button) View ShuffleButton;


    @OnClick({R.id.preview_random_panel})
    void shuffle() {
        mAdapter.shuffle();
    }

    SongChildAdapter mAdapter;
//    PreviewRandomPlayAdapter mPreviewAdapter;

    @OnClick(R.id.refresh)
    void refresh() {
        mRefresh.animate().rotationBy(360).setInterpolator(Animation.getInterpolator(6)).setDuration(650);
        mRefresh.postDelayed(mAdapter::randomize, 300);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_child_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initSortOrder();

        mAdapter = new SongChildAdapter(getActivity());
        mAdapter.setName(TAG);
        mAdapter.setCallBack(this);
        mAdapter.setSortOrderChangedListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        refreshData();
    }

    @Override
    public void onDestroyView() {
        mAdapter.destroy();
        super.onDestroyView();
    }

    private void refreshData() {
    /*    if(getContext() != null)
        SongLoader.doSomething(getContext());
*/
        SongLoader.getAllSongsV2(getActivity(), SortOrderBottomSheet.mSortOrderCodes[mCurrentSortOrder]).subscribe(songs -> {
            mAdapter.setData(songs);
            showOrHidePreview(!songs.isEmpty());
        }, throwable -> {
            Log.e(TAG, throwable.getMessage());
        });
    }

    private void showOrHidePreview(boolean show) {
        int v = show ? View.VISIBLE : View.GONE;

        mRandomGroup.setVisibility(v);
    }

    @Override
    public void onFirstItemCreated(Song song) {
        mTitle.setText(song.title);
        mArtist.setText(song.artistName);

        Glide.with(this)
                .load(Util.getAlbumArtUri(song.albumId))
                .placeholder(R.drawable.music_style)
                .error(R.drawable.music_empty)
                .into(mImage);

    }

    @Override
    public void onPlayingMetaChanged() {
        if (mAdapter != null) mAdapter.notifyOnMediaStateChanged(AbsMediaAdapter.PLAY_STATE_CHANGED);
    }

    @Override
    public void onPaletteChanged() {
        if (mRecyclerView instanceof FastScrollRecyclerView) {
            FastScrollRecyclerView recyclerView = ((FastScrollRecyclerView) mRecyclerView);
            recyclerView.setPopupBgColor(Tool.getHeavyColor());
            recyclerView.setThumbColor(Tool.getHeavyColor());
        }
        mAdapter.notifyOnMediaStateChanged(AbsMediaAdapter.PALETTE_CHANGED);
        super.onPaletteChanged();
    }

    @Override
    public void onPlayStateChanged() {
        if (mAdapter != null) mAdapter.notifyOnMediaStateChanged(AbsMediaAdapter.PLAY_STATE_CHANGED);
    }

    @Override
    public void onMediaStoreChanged() {
        SongLoader.getAllSongs(getActivity(), SortOrderBottomSheet.mSortOrderCodes[mCurrentSortOrder]).subscribe(songs -> {
            mAdapter.setData(songs);
            showOrHidePreview(!songs.isEmpty());
        }, throwable -> {
            Log.e(TAG, throwable.getMessage());
        });
    }

    @Override
    public int getSavedOrder() {
        return mCurrentSortOrder;
    }

    @Override
    public void onOrderChanged(int newType, String name) {
        if (mCurrentSortOrder != newType) {
            mCurrentSortOrder = newType;
            App.getInstance().getPreferencesUtility().setSongChildSortOrder(mCurrentSortOrder);
            refreshData();
        }
    }
}
