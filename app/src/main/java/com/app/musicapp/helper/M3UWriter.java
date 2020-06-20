package com.app.musicapp.helper;

import android.content.Context;
import com.app.musicapp.loader.medialoader.PlaylistSongLoader;
import com.app.musicapp.model.AbsCustomPlaylist;
import com.app.musicapp.model.Playlist;
import com.app.musicapp.model.Song;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class M3UWriter implements M3UConstants {

    public static File write(Context context, File dir, Playlist playlist) throws IOException {
        if (!dir.exists()) //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        File file = new File(dir, playlist.name.concat("." + EXTENSION));

        ArrayList<? extends Song> songs;
        if (playlist instanceof AbsCustomPlaylist) {
            songs = ((AbsCustomPlaylist) playlist).getSongs(context);
        } else {
            songs = PlaylistSongLoader.getPlaylistSongList(context, playlist.id);
        }

        if (songs.size() > 0) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(HEADER);
            for (Song song : songs) {
                bw.newLine();
                bw.write(ENTRY + song.duration + DURATION_SEPARATOR + song.artistName + " - " + song.title);
                bw.newLine();
                bw.write(song.data);
            }

            bw.close();
        }

        return file;
    }
}
