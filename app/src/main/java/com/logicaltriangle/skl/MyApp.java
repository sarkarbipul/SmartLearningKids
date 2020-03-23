package com.logicaltriangle.skl;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import java.io.File;

import com.logicaltriangle.skl.db.AppDatabase;
import com.logicaltriangle.skl.service.RetrofitService;
import com.logicaltriangle.skl.utils.Common;
import retrofit2.Retrofit;

public class MyApp extends Application {
    public String filesDir = "";
    public static String BASE_URL = "http://10.0.2.2/smart_kids_learning/api/";
    public Retrofit retrofit;
    public static RetrofitService retrofitService;
    public AppDatabase appDatabase;
    public Common common;
    public Context context;
    public static String SOUND_PREF_NAME = "sound_pref";
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    static MyApp appState;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        common = new Common(context);
        filesDir = context.getFilesDir() + File.separator;
        appState = this;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "kids_learning")
                .allowMainThreadQueries()
                .build();
        preferences = getSharedPreferences("SKL_PREF", MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static MyApp getInstance() {
        if (appState != null)
            return appState;
        else {
            return new MyApp();
        }
    }
}
