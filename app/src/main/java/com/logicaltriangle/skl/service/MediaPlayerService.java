package com.logicaltriangle.skl.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;

import static com.logicaltriangle.skl.MainActivity.TAG;

public class MediaPlayerService extends IntentService {
    MediaPlayer mediaPlayer;
    String fileDir;
    Context context;

    public MediaPlayerService() {
        super("MediaPlayerService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        context = getApplicationContext();
        fileDir = context.getFilesDir().getAbsolutePath() + File.separator;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (mediaPlayer != null){
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.release();
            } catch (Exception e){}
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String fileName = bundle.getString("filename");
            String filePath = fileDir + fileName;

            Log.d(TAG, "onCreateView: Path: " + filePath);

            File file = new File( filePath );
            if (file.isFile()) {
                Log.d(TAG, "onCreateView: " + "file is ok");
                try {
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } catch (Exception e){

                }
            }
        }

    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null){
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.release();
            } catch (Exception e){}
        }
        super.onDestroy();
    }
}
