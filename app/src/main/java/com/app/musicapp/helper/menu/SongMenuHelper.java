package com.app.musicapp.helper.menu;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import com.app.musicapp.CustomThread.DownloadSongThread;
import com.app.musicapp.R;
import com.app.musicapp.api.providers.EntertainmentProvider;
import com.app.musicapp.helper.songpreview.SongPreviewController;
import com.app.musicapp.loader.medialoader.SongLoader;
import com.app.musicapp.model.Song;
import com.app.musicapp.service.MusicPlayerRemote;
import com.app.musicapp.ui.BaseActivity;
import com.app.musicapp.ui.MainActivity;
import com.app.musicapp.ui.bottomsheet.LyricBottomSheet;
import com.app.musicapp.ui.dialog.AddToPlaylistDialog;
import com.app.musicapp.ui.dialog.DeleteSongsDialog;
import com.app.musicapp.util.MusicUtil;
import com.app.musicapp.util.NavigationUtil;
import com.app.musicapp.util.RingtoneManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Karim Abou Zeid (kabouzeid)
 * modified by Dang Quang Khai (dec25_)
 */
public class SongMenuHelper {
    public static final String TAG = "SongMenuHelper";

    public static final EntertainmentProvider _provider = new EntertainmentProvider();

    @StringRes
    public static final int[] SONG_OPTION = new int[]{
            /*   R.string.play,*/
            R.string.play_next,
            R.string.play_preview,
            R.string.play_preview_all,
            R.string.add_to_queue,
            R.string.add_to_playlist,
            /*    R.string.go_to_source_playlist,*/
            /*        R.string.go_to_album,*/
            R.string.go_to_artist,
            R.string.show_lyric,
            R.string.edit_tag,
            R.string.detail,
            R.string.divider,
            R.string.share,
            R.string.download,
            R.string.set_as_ringtone,
            /*  R.string.delete_from_playlist,*/
            R.string.delete_from_device
    };

    @StringRes
    public static final int[] SONG_QUEUE_OPTION = new int[]{
            R.string.play_next,
            R.string.play_preview,
            R.string.remove_from_queue,
            R.string.add_to_playlist,
            /* R.string.go_to_source_playlist,*/
            /*        R.string.go_to_album,*/
            R.string.go_to_artist,
            R.string.show_lyric,
            R.string.edit_tag,
            R.string.detail,
            R.string.divider,
            R.string.share,
            R.string.set_as_ringtone,
            /*  R.string.delete_from_playlist,*/
            R.string.delete_from_device
    };

    @StringRes
    public static final int[] NOW_PLAYING_OPTION = new int[]{
            R.string.repeat_it_again,
            R.string.play_preview,
            /* R.string.remove_from_queue,*/
            /*  R.string.go_to_source_playlist,*/
            R.string.add_to_playlist,
            /*        R.string.go_to_album,*/
            R.string.go_to_artist,
            R.string.show_lyric,
            R.string.edit_tag,
            R.string.detail,
            R.string.divider,
            R.string.share,
            R.string.set_as_ringtone,
            /*  R.string.delete_from_playlist,*/
            R.string.delete_from_device
    };

    @StringRes
    public static final int[] SONG_ARTIST_OPTION = new int[]{
            /*   R.string.play,*/
            R.string.play_next,
            R.string.play_preview,
            R.string.add_to_queue,
            R.string.add_to_playlist,
            /*    R.string.go_to_source_playlist,*/
            /*        R.string.go_to_album,*/
            /*R.string.go_to_artist,*/
            R.string.show_lyric,
            R.string.edit_tag,
            R.string.detail,
            R.string.divider,
            R.string.share,
            R.string.set_as_ringtone,
            /*  R.string.delete_from_playlist,*/
            R.string.delete_from_device
    };

    public static boolean handleMenuClick(@NonNull AppCompatActivity activity, @NonNull Song song, int string_res_option) {
        switch (string_res_option) {
            case R.string.play_preview:
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).getSongPreviewController().previewSongs(song);
                }
                break;
            case R.string.play_preview_all:
                if (activity instanceof BaseActivity) {
                    SongPreviewController preview = ((MainActivity) activity).getSongPreviewController();
                    if (preview != null) {
                        if (preview.isPlayingPreview())
                            preview.cancelPreview();
                        else {
                            SongLoader.getAllSongs(activity).subscribe(songs -> {
                                ArrayList<Song> list = songs;
                                Collections.shuffle(list);
                                int index = 0;
                                for (int i = 0; i < list.size(); i++) {
                                    if (song.id == list.get(i).id) index = i;
                                }

                                if (index != 0)
                                    list.add(0, list.remove(index));
                                preview.previewSongs(list);
                            }, throwable -> {
                                Log.e(TAG, throwable.getMessage());
                            });
                        }
                    }
                }
                break;
            case R.string.set_as_ringtone:
                if (RingtoneManager.requiresDialog(activity)) {
                    RingtoneManager.showDialog(activity);
                } else {
                    RingtoneManager ringtoneManager = new RingtoneManager();
                    ringtoneManager.setRingtone(activity, song.id);
                }
                return true;
            case R.string.share:
                activity.startActivity(Intent.createChooser(MusicUtil.createShareSongFileIntent(song, activity), null));
                return true;
            case R.string.delete_from_device:
                DeleteSongsDialog.create(song).show(activity.getSupportFragmentManager(), "DELETE_SONGS");
                return true;
            case R.string.add_to_playlist:
                AddToPlaylistDialog.create(song).show(activity.getSupportFragmentManager(), "ADD_PLAYLIST");
                return true;
            case R.string.repeat_it_again:
            case R.string.play_next:
                MusicPlayerRemote.playNext(song);
                return true;
            case R.string.add_to_queue:
                MusicPlayerRemote.enqueue(song);
                return true;
            case R.string.show_lyric:
                LyricBottomSheet.newInstance(song).show(activity.getSupportFragmentManager(), LyricBottomSheet.TAG);
                break;
            case R.string.edit_tag:
                return true;
            case R.string.detail:
                //  SongDetailDialog.create(song).show(activity.getSupportFragmentManager(), "SONG_DETAILS");
                return true;
            case R.string.go_to_album:
                // NavigationUtil.goToAlbum(activity, song.albumId);
                return true;
            case R.string.go_to_artist:
                NavigationUtil.navigateToArtist(activity, song.artistId);
                return true;
            case R.string.download:
                Toast.makeText(activity, activity.getResources().getString(R.string.downloading_song), Toast.LENGTH_LONG).show();
                DownloadSongThread p = new DownloadSongThread(143, song, activity);
                p.start();
                return true;
        }
        return false;
    }

    public static int[] filterSongOption(boolean isWeb) {
        List<Integer> lstSong = Arrays.stream(SongMenuHelper.SONG_OPTION).boxed().collect(Collectors.toList());
        if (!isWeb) {
            lstSong = lstSong.stream()
                    .filter(i -> i != R.string.download)
                    .collect(Collectors.toList());
        } else {
            lstSong = lstSong.stream()
                    .filter(i -> i != R.string.set_as_ringtone && i != R.string.delete_from_device)
                    .collect(Collectors.toList());
        }
        return lstSong.stream().mapToInt(i -> i).toArray();
    }


}
