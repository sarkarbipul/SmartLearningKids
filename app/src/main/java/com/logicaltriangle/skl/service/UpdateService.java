package com.logicaltriangle.skl.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import com.logicaltriangle.skl.MainActivity;
import com.logicaltriangle.skl.MyApp;
import com.logicaltriangle.skl.model.CatResult;
import com.logicaltriangle.skl.model.Category;
import com.logicaltriangle.skl.model.Download;
import com.logicaltriangle.skl.model.Item;
import com.logicaltriangle.skl.model.ItemResult;
import com.logicaltriangle.skl.model.Word;
import com.logicaltriangle.skl.model.WordResult;
import com.logicaltriangle.skl.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.logicaltriangle.skl.MainActivity.TAG;
import static com.logicaltriangle.skl.MainActivity.retrofitService;

public class UpdateService extends IntentService {

    private List<Category> categoryList;
    private List<Item> itemList;
    private List<Word> wordList;

    private Common common;

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        common = new Common(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            fetchRemoteData();
        }
    }

    //fetching remote data
    public void fetchRemoteData() {
        if (common.isNetWorkOk()) {
            //isFetchingRemote = true;

            /*retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);*/

            fetchRemoteCategories();
        }else {
            //showDialog();
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

                        categoryList = MainActivity.appDatabase.getCategoryDao().getAllCategory();

                        for (Category category : response.body().getCatList()){
                            if (searchInList(1, category.getCatName()) == -1)
                                MainActivity.appDatabase.getCategoryDao().insert( category );
                            else {
                                MainActivity.appDatabase.getCategoryDao().update(category);
                            }
                            insertDownload(category.getCatImgUrl());
                            insertDownload(category.getCatAudioUrl());
                        }
                        //Log.d(TAG, "CatInLocalDB: " + categories.size());

                        fetchRemoteItems();
                    }
                }
                //Log.d(TAG, "Cat-Size: " + response.body().getCatList().size() );
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
                Log.d(TAG, "Item-SizeAAA: " + response.body().getItemList().size() );

                //saving remote items into local db
                //write your code here
                itemList = MainActivity.appDatabase.getItemDao().getAllItems();
                for (Item item : response.body().getItemList()){
                    try {
                        if (searchInList(2, item.getItemName()) == -1)
                            MainActivity.appDatabase.getItemDao().insert( item );
                        else {
                            MainActivity.appDatabase.getItemDao().update(item);
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
                wordList = MainActivity.appDatabase.getWordDao().getAllWords();
                for (Word word : response.body().getWordList()){
                    try {
                        if (searchInList(3, word.getWordName1()) == -1)
                            MainActivity.appDatabase.getWordDao().insert( word );
                        else {
                            MainActivity.appDatabase.getWordDao().update(word);
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

                //editor.putBoolean(CONFIG_PREFS_KEY, true).commit();
                //configStatus = true;
                //goToHome();
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
}
