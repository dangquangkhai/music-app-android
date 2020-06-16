package com.app.musicapp.api.providers;

import android.util.Log;
import com.app.musicapp.api.config.ApiConfig;
import com.app.musicapp.api.customModels.ApiResponse;
import com.app.musicapp.api.models.Song;
import com.app.musicapp.api.utils.ReflectionHelper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
import com.fasterxml.jackson.databind.ObjectMapper;
*/

public class EntertainmentProvider {

    public static final String TAG = "Entertainment";
    public List<Song> getAllSongSync() {
        try {
            Observable<Response> fetchDt = Observable.create(
                    (ObservableOnSubscribe<Response>) emitter -> {
                        try {
                            String url = ApiConfig.BACKEND_API + ApiConfig.SONG_API;
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            emitter.onNext(client.newCall(request).execute());
                        } catch (Exception e) {
                            emitter.onError(e); // In case there are network errors
                        }
                    });
            Response _res = fetchDt.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).blockingFirst();
            ApiResponse apiRes = ReflectionHelper._mapper().readValue(_res.body().string(), ApiResponse.class);
            if (!apiRes.getSuccess()) {
                throw new Exception("Can't get list song");
            }
            List<HashMap<String, Object>> lstSong = new ArrayList<>();
            List<Song> lstResult = new ArrayList<>();
            lstSong = ReflectionHelper._mapper().convertValue(apiRes.getData(), lstSong.getClass());
            for (HashMap<String, Object> item : lstSong) {
                Song newItem = ReflectionHelper._mapper().convertValue(item, Song.class);
                lstResult.add(newItem);
            }
            return lstResult;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new ArrayList<>();
        }

    }
    public Song getSongByIdSync(int SongId)
    {
        try {
            Observable<Response> fetchDt = Observable.create(
                    (ObservableOnSubscribe<Response>) emitter -> {
                        try {
                            String url = ApiConfig.BACKEND_API + ApiConfig.SONG_API + "/" + SongId;
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .build();
                            emitter.onNext(client.newCall(request).execute());
                        } catch (Exception e) {
                            emitter.onError(e); // In case there are network errors
                        }
                    });
            Response _res = fetchDt.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).blockingFirst();
            ApiResponse apiRes = ReflectionHelper._mapper().readValue(_res.body().string(), ApiResponse.class);
            if (!apiRes.getSuccess()) {
                throw new Exception("Can't get song data");
            }
            Song _song = new Song();
            _song = ReflectionHelper._mapper().convertValue(apiRes.getData(), Song.class);
            return _song;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return new Song();
        }
    }
}