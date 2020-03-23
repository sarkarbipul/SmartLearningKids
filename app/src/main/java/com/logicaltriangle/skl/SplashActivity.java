package com.logicaltriangle.skl;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import com.logicaltriangle.skl.db.AppDatabase;
import com.logicaltriangle.skl.model.CatResult;
import com.logicaltriangle.skl.model.Category;
import com.logicaltriangle.skl.model.Download;
import com.logicaltriangle.skl.model.Item;
import com.logicaltriangle.skl.model.ItemResult;
import com.logicaltriangle.skl.model.Word;
import com.logicaltriangle.skl.model.WordResult;
import com.logicaltriangle.skl.service.RetrofitService;
import com.logicaltriangle.skl.utils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {

    public static String TAG = "smartkidslearningABC";

    ImageView icon;
    Animation scale_up;

    private boolean isFetchingRemote = false;

    private LinearLayout lLConfig;

    private SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static String SKL_PREFS_NAME = "smart_kids_learing";
    public static String CONFIG_PREFS_KEY = "config_status";
    private boolean configStatus;

    //Retrofit declaration
    Retrofit retrofit;
    RetrofitService retrofitService;

    public static AppDatabase appDatabase;

    //public static String BASE_URL = "https://usurped-puddle.000webhostapp.com/kids_learning/api/";
    public static String BASE_URL = "http://mstpharmabd.com/apps/smart_kids_learning/api/";

    private List<Category> categoryList;
    private List<Item> itemList;
    private List<Word> wordList;

    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //initiating
        common = new Common(SplashActivity.this);

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "kids_learning")
                .allowMainThreadQueries()
                .build();

        //init preferences
        preferences = getSharedPreferences(SKL_PREFS_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        //getting preferences values
        configStatus = preferences.getBoolean(CONFIG_PREFS_KEY, false);

        //initiating data
        configData();

        //finding lLConfig
        lLConfig = findViewById(R.id.lLConfig);

        icon = findViewById(R.id.icon);
        scale_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
        icon.startAnimation(scale_up);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFetchingRemote == false)
                    goToHome();
            }
        }, 3000);
    }

    public void goToHome() {
        if (configStatus) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    //calling configuration
    public void configData() {

        if (configStatus == true) {
            //getting local data
            boolean isLocalDataOk = fetchLocalData();
            if (!isLocalDataOk){
                fetchRemoteData();
            }
        } else {
            //fetching remote data
            fetchRemoteData();
        }

    }

    //fetcing local
    public boolean fetchLocalData() {

        categoryList = appDatabase.getCategoryDao().getAllCategory();
        itemList = appDatabase.getItemDao().getAllItems();

        if (categoryList.size() > 0 && itemList.size() > 0)
            return true;

        return false;
    }

    //fetching remote data
    public void fetchRemoteData() {
        if (common.isNetWorkOk()) {
            isFetchingRemote = true;

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);

            fetchRemoteCategories();
        }else {
            showDialog();
        }
    }

    //fetching reomote category
    public void fetchRemoteCategories() {
        Call<CatResult> catResultCall = retrofitService.getCatResult();
        catResultCall.enqueue(new Callback<CatResult>() {
            @Override
            public void onResponse(Call<CatResult> call, Response<CatResult> response) {
                if (response.isSuccessful()){
                    if (response.body().isStatus()) {

                        //saving remote categories into local db
                        //write your code here

                        categoryList = appDatabase.getCategoryDao().getAllCategory();

                        for (Category category : response.body().getCatList()){
                            if (searchInList(1, category.getCatName()) == -1) {
                                appDatabase.getCategoryDao().insert(category);
                            }else {
                                appDatabase.getCategoryDao().update(category);
                            }
                            insertDownload(category.getCatImgUrl());
                            insertDownload(category.getCatAudioUrl());
                        }
                        //Log.d(TAG, "CatInLocalDB: " + categories.size());

                        fetchRemoteItems();
                    }
                }
                Log.d(TAG, "Cat-Size: " + response.body().getCatList().size() );
            }

            @Override
            public void onFailure(Call<CatResult> call, Throwable t) {
                Log.d(TAG, t.getMessage() );
            }
        });
    }



    //remote items
    public void fetchRemoteItems() {
        Call<ItemResult> itemResultCall = retrofitService.getItemResult();
        itemResultCall.enqueue(new Callback<ItemResult>() {
            @Override
            public void onResponse(Call<ItemResult> call, Response<ItemResult> response) {
                Log.d(TAG, "Item-Size: " + response.body().getItemList().size() );

                //saving remote items into local db
                //write your code here
                itemList = appDatabase.getItemDao().getAllItems();
                for (Item item : response.body().getItemList()){
                    try {
                        if (searchInList(2, item.getItemName()) == -1)
                            appDatabase.getItemDao().insert( item );
                        else {
                            appDatabase.getItemDao().update(item);
                        }
                        insertDownload(item.getItemImgUrl());
                        insertDownload(item.getItemAudioUrl());
                    } catch (Exception ex){

                    }
                }

                //fetching words
                fetchRemoteWords();

                //editor.putBoolean(CONFIG_PREFS_KEY, true).commit();
                //configStatus = true;
                //goToHome();
            }

            @Override
            public void onFailure(Call<ItemResult> call, Throwable t) {

            }
        });
    }

    //remote words
    public void fetchRemoteWords() {
        Call<WordResult> wordResultCall = retrofitService.getWordResult();
        wordResultCall.enqueue(new Callback<WordResult>() {
            @Override
            public void onResponse(Call<WordResult> call, Response<WordResult> response) {
                //Log.d(TAG, "Item-Size: " + response.body().getItemList().size() );

                //saving remote items into local db
                //write your code here
                wordList = appDatabase.getWordDao().getAllWords();
                for (Word word : response.body().getWordList()){
                    try {
                        if (searchInList(3, word.getWordName1()) == -1)
                            appDatabase.getWordDao().insert( word );
                        else {
                            appDatabase.getWordDao().update(word);
                        }
                        insertDownload(word.getWordImg1());
                        insertDownload(word.getWordAudio1());

                        insertDownload(word.getWordImg2());
                        insertDownload(word.getWordAudio2());

                        insertDownload(word.getWordImg3());
                        insertDownload(word.getWordAudio3());

                        insertDownload(word.getWordImg4());
                        insertDownload(word.getWordAudio4());
                    } catch (Exception ex){

                    }
                }

                editor.putBoolean(CONFIG_PREFS_KEY, true).commit();
                configStatus = true;
                goToHome();
            }

            @Override
            public void onFailure(Call<WordResult> call, Throwable t) {

            }
        });
    }

    private void insertDownload(String downloadUrl) {
        Download download = MyApp.getInstance().appDatabase.getDownloadDao().getDownloadByUrl(downloadUrl);
        if (download == null) {
            download = new Download();
            download.setDownloadId(0);
            download.setFileUrl(downloadUrl);
            MyApp.getInstance().appDatabase.getDownloadDao().insert(download);
        }
    }

    //serch in list
    public int searchInList(int option, String name) {
        if (option == 1) {
            for (Category cat : categoryList) {
                if (cat.getCatName().equals(name))
                    return cat.getCatId();
            }
        } else if(option == 2) {
            for (Item item : itemList) {
                if (item.getItemName().equals(name))
                    return item.getItemId();
            }
        } else if(option == 3) {
            for (Word word : wordList) {
                if (word.getWordName1().equals(name))
                    return word.getWordId();
            }
        }
        return -1;
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

        builder.setTitle("Warning!");
        builder.setMessage("Check your internet connection & Try again");
        builder.setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchRemoteData();
            }
        });
        builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
