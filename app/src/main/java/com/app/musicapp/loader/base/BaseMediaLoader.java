package com.app.musicapp.loader.base;

import android.content.Context;
import android.os.Process;
import androidx.annotation.NonNull;
import com.app.musicapp.loader.manager.PriorityThreadFactory;
import com.app.musicapp.util.PreferenceUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BaseMediaLoader {
    private static final String TAG = "BaseMediaStoreManager";

    private Context mContext;

    public BaseMediaLoader() {
    }

    private ThreadPoolExecutor mExecutor;
    private int mThreadNumber = 6;

    public void init(@NonNull Context context) {
        mContext = context;
        mThreadNumber = PreferenceUtil.getInstance().getThreadNumber();
        if (mExecutor != null) {
            ThreadFactory factory = new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);
            mExecutor = new ThreadPoolExecutor(
                    mThreadNumber,
                    mThreadNumber * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    factory);
        }

    }

    public void destroy() {
        mContext = null;
        if (mExecutor != null)
            mExecutor.shutdown();
        mExecutor = null;
    }
}
