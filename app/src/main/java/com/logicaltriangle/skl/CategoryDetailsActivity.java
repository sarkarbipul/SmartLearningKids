package com.logicaltriangle.skl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;
import java.util.List;

import com.logicaltriangle.skl.adapter.ItemListAdapter;
import com.logicaltriangle.skl.model.Download;
import com.logicaltriangle.skl.model.Item;
import com.logicaltriangle.skl.utils.Common;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.logicaltriangle.skl.MainActivity.TAG;

public class CategoryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static ViewPager2 viewPager;
    LinearLayout fragmentContainerLo;
    public static MediaPlayer mediaPlayer;
    public static Context context;

    //number range container
    public static LinearLayout lLNumberRange;
    CircleImageView number_back_btn, number_sound_btn;

    Button btnRange1, btnRange2, btnRange3, btnRange4, btnRange5, btnRange6, btnRange7, btnRange8, btnRange9, btnRange10;
    int catID;
    String catName;
    int indexRange;
    List<Item> itemList;
    public static String runningFileName = "";

    private Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        context = this;
        common = new Common(context);

        viewPager = findViewById(R.id.viewPagerId);
        fragmentContainerLo = findViewById(R.id.fragmentContainerLoId);
        lLNumberRange = findViewById(R.id.lLNumberRange);

        //init and set listeners
        initViews();

        //init ItemListAdapter
        catID = getIntent().getIntExtra("categoryID", 0);
        catName = getIntent().getStringExtra("categoryName");

        if (catName.contains("Numbers")) {
            viewPager.setVisibility(View.GONE);
            lLNumberRange.setVisibility(View.VISIBLE);
        } else {
            lLNumberRange.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

            setUpViewPager(0);
        }
    }

    public void setUpViewPager(int indexR) {

        if (catName.contains("Numbers")) {
            String from = "" + (indexR * 10 - 9);
            if (from.equals("1"))
                from = "0";
            String to = "" + (indexR * 10);
            itemList = MyApp.getInstance().appDatabase.getItemDao().getItemsByFromTo(catID, from, to);
        } else {
            itemList = MyApp.getInstance().appDatabase.getItemDao().getItemsByCatId(catID);
        }

        ItemListAdapter itemListAdapter = new ItemListAdapter(this, catID, catName, indexRange, itemList);
        viewPager.setAdapter(itemListAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Item item = itemList.get(position);
                String filename = common.getFileNameFromUrl(item.getItemAudioUrl());
                if (filename != "") {
                    playMedia(item.getItemAudioUrl());
                }
                Log.d(TAG, "onPageSelected : " + position + " filname: " + filename);
            }
        });
    }

    private void initViews() {
        number_back_btn = findViewById(R.id.number_back_btn);
        number_sound_btn = findViewById(R.id.number_sound_btn);
        btnRange1 = findViewById(R.id.btnRange1);
        btnRange2 = findViewById(R.id.btnRange2);
        btnRange3 = findViewById(R.id.btnRange3);
        btnRange4 = findViewById(R.id.btnRange4);
        btnRange5 = findViewById(R.id.btnRange5);
        btnRange6 = findViewById(R.id.btnRange6);
        btnRange7 = findViewById(R.id.btnRange7);
        btnRange8 = findViewById(R.id.btnRange8);
        btnRange9 = findViewById(R.id.btnRange9);
        btnRange10 = findViewById(R.id.btnRange10);

        number_back_btn.setOnClickListener(this::onClick);
        number_sound_btn.setOnClickListener(this::onClick);
        btnRange1.setOnClickListener(this::onClick);
        btnRange2.setOnClickListener(this::onClick);
        btnRange3.setOnClickListener(this::onClick);
        btnRange4.setOnClickListener(this::onClick);
        btnRange5.setOnClickListener(this::onClick);
        btnRange6.setOnClickListener(this::onClick);
        btnRange7.setOnClickListener(this::onClick);
        btnRange8.setOnClickListener(this::onClick);
        btnRange9.setOnClickListener(this::onClick);
        btnRange10.setOnClickListener(this::onClick);

        //setting sound icon
        if (MyApp.getInstance().preferences.getInt(MyApp.SOUND_PREF_NAME, 1) == 1) {
            number_sound_btn.setBackgroundResource(R.drawable.number_sound_button);
        } else {
            number_sound_btn.setBackgroundResource(R.drawable.number_sound_off);
        }

    }

    public static void playMedia(String urlHavingFileName) {

        String fileName = MyApp.getInstance().common.getFileNameFromUrl(urlHavingFileName);
        runningFileName = fileName;

        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.release();
            } catch (Exception e) {
            }
        }
        String fileDir = context.getFilesDir() + File.separator;
        String filePath = fileDir + fileName;
        Log.d(TAG, "onCreateView: Path: " + filePath);

        File file = new File(filePath);
        if (MyApp.getInstance().preferences.getInt(MyApp.SOUND_PREF_NAME, 1) == 1)
            if (file.isFile()) {
                Log.d(TAG, "onCreateView: " + "file is ok");
                try {
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(filePath));
                    //mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } catch (Exception e) {
                }
            } else {
                if (!urlHavingFileName.isEmpty())
                    downLoadFile(urlHavingFileName);
            }
    }

    public static void releaseMedia() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.release();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMedia();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        indexRange = 1;
        String idResName = getResources().getResourceName(v.getId());
        idResName = idResName.substring(idResName.lastIndexOf("/") + 1);
        if (idResName.contains("btnRange")) {
            indexRange = Integer.parseInt(idResName.substring(8));

            Log.d(TAG, "onClick: " + indexRange);

            lLNumberRange.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

            setUpViewPager(indexRange);

        } else if (viewId == R.id.number_back_btn) {
            CategoryDetailsActivity.this.finish();
        } else if(viewId == R.id.number_sound_btn){
            if (MyApp.getInstance().preferences.getInt(MyApp.SOUND_PREF_NAME, 1) == 1) {
                number_sound_btn.setBackgroundResource(R.drawable.number_sound_off);
                MyApp.getInstance().editor.putInt(MyApp.SOUND_PREF_NAME, 0).commit();
                CategoryDetailsActivity.playMedia("");
            } else {
                number_sound_btn.setBackgroundResource(R.drawable.number_sound_button);
                MyApp.getInstance().editor.putInt(MyApp.SOUND_PREF_NAME, 1).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (lLNumberRange.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else if (catName.contains("Numbers")) {
            viewPager.setVisibility(View.GONE);
            lLNumberRange.setVisibility(View.VISIBLE);
            releaseMedia();
        } else {
            super.onBackPressed();
        }
    }

    public static void downLoadFile(String url) {
        String fileName = MyApp.getInstance().common.getFileNameFromUrl(url);
        String dirPath = MyApp.getInstance().filesDir;

        File file = new File(dirPath + fileName);

        //if no file, then download
        if (!file.isFile()) {
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
                            Log.d(TAG, "onProgress: " + progress.currentBytes + "/" + progress.totalBytes);
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            //download next file
                            //downloadNext();
                            if (runningFileName.equals(fileName))
                                playMedia(url);
                        }

                        @Override
                        public void onError(Error error) {
                            //download next file
                            //downloadNext();
                        }
                    });


            //need to check prev record=================
            Download prevDownload = MyApp.getInstance().appDatabase
                    .getDownloadDao()
                    .getDownloadByUrl(url);

            if (prevDownload == null) {
                //adding download to local db
                Download download = new Download();
                download.setFileUrl(url);
                download.setDownloadId(downloadId);
                MyApp.getInstance().appDatabase.getDownloadDao().insert(download);
            } else {
                prevDownload.setDownloadId(downloadId);
                MyApp.getInstance().appDatabase.getDownloadDao().update(prevDownload);
            }
        }
    }

    public static void bounceAnim(ImageView imageView) {

        int duration = 2000;
        /*if (mediaPlayer != null)
            duration = mediaPlayer.getDuration();*/

        Animation bounce = AnimationUtils.loadAnimation(context, R.anim.bounce);
        bounce.setRepeatMode(Animation.REVERSE);
        bounce.setDuration(duration);
        imageView.startAnimation(bounce);
    }
}
