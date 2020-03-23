package com.logicaltriangle.skl.fragment;

import android.content.res.Resources;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import com.logicaltriangle.skl.CategoryDetailsActivity;
import com.logicaltriangle.skl.MyApp;
import com.logicaltriangle.skl.R;
import com.logicaltriangle.skl.model.Word;
import com.logicaltriangle.skl.utils.Common;

public class AlphabetFragment extends Fragment implements View.OnClickListener {

    private Button leftTopBtn, rightTopBtn, leftBottomBtn, rightBottomBtn, topBackArrow, topRightSound;
    private ImageView centerIv;
    int itemId = 0;
    String itemImgUrl = "";
    String itemAudioUrl = "";

    private RequestBuilder<PictureDrawable> requestBuilder;

    private Common common;

    Word word;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alphabet, container, false);
        topBackArrow = view.findViewById(R.id.topBackArrowId);
        topRightSound = view.findViewById(R.id.topRightSoundId);
        leftTopBtn = view.findViewById(R.id.leftTopBtnId);
        rightTopBtn = view.findViewById(R.id.rightTopBtnId);
        leftBottomBtn = view.findViewById(R.id.leftBottomBtnId);
        rightBottomBtn = view.findViewById(R.id.rightBottomBtnId);
        centerIv = view.findViewById(R.id.centerIvId);

        topBackArrow.setOnClickListener(this::onClick);
        topRightSound.setOnClickListener(this::onClick);
        leftTopBtn.setOnClickListener(this::onClick);
        rightTopBtn.setOnClickListener(this::onClick);
        leftBottomBtn.setOnClickListener(this::onClick);
        rightBottomBtn.setOnClickListener(this::onClick);
        centerIv.setOnClickListener(this::onClick);

        //initiating common
        common = new Common(getActivity());
        //filesDirPath = getActivity().getFilesDir().getAbsolutePath() + File.separator;

        itemId = getArguments().getInt("ItemID");
        itemImgUrl = getArguments().getString("ItemImgUrl");
        itemAudioUrl = getArguments().getString("itemAudioUrl");

        if (itemId != 0) {

            try {
                word = MyApp.getInstance().appDatabase.getWordDao().getWordByItemId(itemId);
                if (word != null) {
                    leftTopBtn.setText(word.getWordName1());
                    rightTopBtn.setText(word.getWordName2());
                    leftBottomBtn.setText(word.getWordName3());
                    rightBottomBtn.setText(word.getWordName4());

                    if (word.getWordName3().contains("Vervet"))
                        leftBottomBtn.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    else {
                        // Converts 14 dip into its equivalent px
                        float dip = 60f;
                        Resources r = getResources();
                        float px = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                dip,
                                r.getDisplayMetrics()
                        );
                        leftBottomBtn.setHeight((int) px);
                    }
                } else {
                    Log.d("Tag1111", "Word is null");
                }
            } catch (Exception e) {
                Log.d("Tag1111", e.getMessage());
            }

        }

        //setting center image
        setCenterIv(itemImgUrl);

        //setting sound icon
        if (MyApp.getInstance().preferences.getInt(MyApp.SOUND_PREF_NAME, 1) == 1) {
            topRightSound.setBackgroundResource(R.drawable.ic_sound);
        } else {
            topRightSound.setBackgroundResource(R.drawable.ic_sound_off);
        }

        return view;
    }

    private void setCenterIv(String imgUrl) {
        String fileName = MyApp.getInstance().common.getFileNameFromUrl(imgUrl);
        if (fileName != "") {
            if (fileName.contains("/"))
                fileName.replace("/", "");
            try {
                String filePath = MyApp.getInstance().filesDir + fileName;
                File file = new File(filePath);
                if (file.isFile()) {
                    Glide.with(getActivity())
                            .load(Uri.fromFile(file))
                            .into(centerIv);
                } else {
                    Glide.with(getActivity())
                            .load(Uri.parse(imgUrl))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(centerIv);

                    CategoryDetailsActivity.downLoadFile(imgUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftTopBtnId:
                if (word != null) {
                    //setting center image
                    setCenterIv(word.getWordImg1());

                    //playing audio
                    CategoryDetailsActivity.runningFileName = common.getFileNameFromUrl(word.getWordAudio1());
                    CategoryDetailsActivity.playMedia(word.getWordAudio1());
                    CategoryDetailsActivity.bounceAnim(centerIv);
                }
                break;
            case R.id.rightTopBtnId:
                if (word != null) {
                    //setting center image
                    setCenterIv(word.getWordImg2());

                    //playing audio
                    CategoryDetailsActivity.runningFileName = common.getFileNameFromUrl(word.getWordAudio2());
                    CategoryDetailsActivity.playMedia(word.getWordAudio2());
                    CategoryDetailsActivity.bounceAnim(centerIv);
                }
                break;
            case R.id.leftBottomBtnId:
                if (word != null) {
                    //setting center image
                    setCenterIv(word.getWordImg3());

                    //playing audio
                    CategoryDetailsActivity.runningFileName = common.getFileNameFromUrl(word.getWordAudio3());
                    CategoryDetailsActivity.playMedia(word.getWordAudio3());
                    CategoryDetailsActivity.bounceAnim(centerIv);
                }
                break;
            case R.id.rightBottomBtnId:
                if (word != null) {
                    //setting center image
                    setCenterIv(word.getWordImg4());

                    //playing audio
                    CategoryDetailsActivity.runningFileName = common.getFileNameFromUrl(word.getWordAudio4());
                    CategoryDetailsActivity.playMedia(word.getWordAudio4());
                    CategoryDetailsActivity.bounceAnim(centerIv);
                }
                break;
            case R.id.topBackArrowId:
                CategoryDetailsActivity.runningFileName = "";
                getActivity().finish();
                break;
            case R.id.topRightSoundId:
                if (MyApp.getInstance().preferences.getInt(MyApp.SOUND_PREF_NAME, 1) == 1) {
                    topRightSound.setBackgroundResource(R.drawable.ic_sound_off);
                    MyApp.getInstance().editor.putInt(MyApp.SOUND_PREF_NAME, 0).commit();
                    CategoryDetailsActivity.playMedia("");
                } else {
                    topRightSound.setBackgroundResource(R.drawable.ic_sound);
                    MyApp.getInstance().editor.putInt(MyApp.SOUND_PREF_NAME, 1).commit();
                }
                break;
            case R.id.centerIvId:
                String fileName = MyApp.getInstance().common.getFileNameFromUrl(itemAudioUrl);
                CategoryDetailsActivity.playMedia(itemAudioUrl);
                CategoryDetailsActivity.bounceAnim(centerIv);
                break;
            default:
                CategoryDetailsActivity.runningFileName = "";
                break;
        }
    }
}
