package com.logicaltriangle.skl.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.logicaltriangle.skl.MainActivity;
import com.logicaltriangle.skl.MyApp;
import com.logicaltriangle.skl.model.Download;

public class DownloadService extends IntentService {

    List<String> fileUrlList = new ArrayList<>();
    int index = 0;
    String TAG = "SVCSVC";
    String selectedUrl = "";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("SVCSVC", "Starting Service");

        try {
            selectedUrl = intent.getStringExtra("selectedUrl");
            if (selectedUrl == null)
                selectedUrl = "";
        } catch (Exception e) {
            //e.printStackTrace();
        }

        if (selectedUrl.isEmpty()) {
            readAllUrlsFromDB();
            if (fileUrlList.size() > 0) {
                downLoadFile( fileUrlList.get(index) );
            }
        } else {
            //download single file
            downLoadFile( selectedUrl );
        }
    }

    //read urls from database
    private void readAllUrlsFromDB() {
        List<Download> downloads = MyApp.getInstance().appDatabase.getDownloadDao().getAllDownloads();
        for (Download download:downloads) {
            if (isValidFileLink(download.getFileUrl()))
                fileUrlList.add(download.getFileUrl());
        }
        /*List<Category> categoryList =
                MainActivity.appDatabase.getCategoryDao().getAllCategory();
        for (Category category : categoryList){
            if (isValidFileLink(category.getCatImgUrl()))
                fileUrlList.add(category.getCatImgUrl());

            if (isValidFileLink(category.getCatAudioUrl()))
                fileUrlList.add(category.getCatAudioUrl());
        }

        //get all item's urls
        List<Item> itemList = MainActivity.appDatabase.getItemDao().getAllItems();
        for (Item item : itemList){
            if (isValidFileLink(item.getItemImgUrl()))
                fileUrlList.add(item.getItemImgUrl());

            if (isValidFileLink(item.getItemAudioUrl()))
                fileUrlList.add(item.getItemAudioUrl());
        }

        //get all word's urls
        List<Word> wordList = MainActivity.appDatabase.getWordDao().getAllWords();
        for (Word word : wordList) {
            if (isValidFileLink(word.getWordImg1()))
                fileUrlList.add(word.getWordImg1());
            if (isValidFileLink(word.getWordImg2()))
                fileUrlList.add(word.getWordImg2());
            if (isValidFileLink(word.getWordImg3()))
                fileUrlList.add(word.getWordImg3());
            if (isValidFileLink(word.getWordImg4()))
                fileUrlList.add(word.getWordImg4());

            if (isValidFileLink(word.getWordAudio1()))
                fileUrlList.add(word.getWordAudio1());
            if (isValidFileLink(word.getWordAudio2()))
                fileUrlList.add(word.getWordAudio2());
            if (isValidFileLink(word.getWordAudio3()))
                fileUrlList.add(word.getWordAudio3());
            if (isValidFileLink(word.getWordAudio4()))
                fileUrlList.add(word.getWordAudio4());
        }*/
    }

    public void downloadNext() {
        try {
            Thread.sleep(1);
            if (index < fileUrlList.size() && selectedUrl.isEmpty())
                downLoadFile( fileUrlList.get(index) );

            Log.d(TAG, "downloadNext: " + index + " - " + fileUrlList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void downLoadFile(String url) {
        index++;
        String fileName = "";
        if (isValidFileLink(url) && index < fileUrlList.size()) {
            fileName = getFileNameFromUrl(url);
            String dirPath = getFilesDir().getAbsolutePath() + File.separator;

            File file = new File(dirPath + fileName);

            //if no file, then download
            if (!file.isFile()){
                //need to check prev record=================
                Download prevDownload = MainActivity.appDatabase
                        .getDownloadDao()
                        .getDownloadByUrl(url);

                int downloadId = PRDownloader.download(url, dirPath, fileName)
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {

                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                Log.d(TAG, "onProgress: " + index + " : " + progress.currentBytes + "/" + progress.totalBytes);
                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                //download next file
                                downloadNext();
                            }
                            @Override
                            public void onError(Error error) {
                                //download next file
                                downloadNext();
                            }
                        });

                if (prevDownload == null) {
                    //adding download to local db
                    Download download = new Download();
                    download.setFileUrl(url);
                    download.setDownloadId(downloadId);
                    MainActivity.appDatabase.getDownloadDao().insert(download);
                } else {
                    prevDownload.setDownloadId(downloadId);
                    MainActivity.appDatabase.getDownloadDao().update(prevDownload);
                }

            } else {
                //download next file
                downloadNext();
            }
        } else {
            //download next file
            downloadNext();
        }
    }

    //Must be called if link is valid
    private String getFileNameFromUrl(String fileUrl) {
        String fileName = fileUrl.substring( fileUrl.lastIndexOf("/") );
        if (fileName.contains("/"))
            fileName = fileName.replace("/", "");
        return fileName;
    }

    //Checking validity of file link
    private boolean isValidFileLink(String fileUrl) {
        if (fileUrl != null)
            if (!fileUrl.isEmpty()) {
                if (fileUrl.contains("/")) {
                    String fileName = getFileNameFromUrl(fileUrl);
                    if (!fileName.isEmpty())
                        if (fileName.contains("."))
                            return true;
                }
            }

        return false;
    }
}
