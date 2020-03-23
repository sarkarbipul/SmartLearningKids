package com.logicaltriangle.skl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import java.io.IOException;
import java.util.List;

import com.logicaltriangle.skl.adapter.CategoryListAdapter;
import com.logicaltriangle.skl.dao.CategoryDao;
import com.logicaltriangle.skl.dao.ItemDao;
import com.logicaltriangle.skl.db.AppDatabase;
import com.logicaltriangle.skl.model.Category;
import com.logicaltriangle.skl.service.DownloadService;
import com.logicaltriangle.skl.service.RetrofitService;
import com.logicaltriangle.skl.service.UpdateService;
import com.logicaltriangle.skl.utils.Common;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static AppDatabase appDatabase;

    public static String TAG = "smartkidslearningABC";

    public static String BASE_URL = "http://mstpharmabd.com/apps/smart_kids_learning/api/";

    public RecyclerView categoriesRV;
    public CategoryListAdapter categoryAdapter;
    //public LinearLayoutManager linearLayoutManager;

    public static ImageView categoriesIV;
    public static TextView categoriesTV;

    //public CircleImageView category_sound_btn;
    public ImageView dev_info;

    //Retrofit declaration
    Retrofit retrofit;
    public static RetrofitService retrofitService;

    //conn
    Common common;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        common = new Common(this);
        if (common.isNetWorkOk()){
            Intent intent = new Intent(MainActivity.this, UpdateService.class);
            try {
                startService(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Internet..", Toast.LENGTH_SHORT).show();
        }

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "kids_learning")
                .allowMainThreadQueries()
                .build();

        CategoryDao categoryDao = appDatabase.getCategoryDao();
        ItemDao itemDao = appDatabase.getItemDao();

        /*Category category = new Category();
        category.setCatName("Category 1");
        categoryDao.insert(category);*/

        appDatabase.getCategoryDao().deleteLikeCats("%Cat%");

        List<Category> categories = categoryDao.getAllCategory();
        Log.d(TAG, "cats: " + categories.size());


        categoriesRV = findViewById(R.id.categoriesRV);
        //category_sound_btn = findViewById(R.id.category_sound_btn);

        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //categoriesRV.setLayoutManager(linearLayoutManager);
        categoriesRV.setHasFixedSize(true);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        //getting data
        getCategory();

        //PR Downloader
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        //creating and starting download service
        startDownloadSvc();

        dev_info = findViewById(R.id.dev_info);
        dev_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DevInfoActivity.class));
            }
        });
    }

    //starting svc
    private void startDownloadSvc() {
        if (common.isNetWorkOk()){
            try {
                Intent dwdIntent = new Intent(this, DownloadService.class);
                startService(dwdIntent);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    //checking local data
    public void getCategory() {
        //fetching local data
        List<Category> categoryList = MyApp.getInstance().appDatabase.getCategoryDao().getAllCategory();
        try {
            initAndSetAdapter( categoryList );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Adapter setup
    public void initAndSetAdapter(List<Category> categoryList) throws IOException {
        //initializing adapter
        categoryAdapter = new CategoryListAdapter(MainActivity.this, categoryList);
        //set adapter to recyclerview
        categoriesRV.setAdapter(categoryAdapter);

        /*String fileLocation = getFilesDir().getAbsolutePath() + File.separator + "item_audio_1582787708.mp3";
        File file = new File(fileLocation);*/

        /*MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(fileLocation);
        mediaPlayer.prepare();
        mediaPlayer.start();*/
    }
}
