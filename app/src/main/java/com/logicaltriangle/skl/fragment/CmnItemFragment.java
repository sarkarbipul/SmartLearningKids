package com.logicaltriangle.skl.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import com.logicaltriangle.skl.CategoryDetailsActivity;
import com.logicaltriangle.skl.MyApp;
import com.logicaltriangle.skl.R;
import com.logicaltriangle.skl.adapter.ItemListAdapter;
import com.logicaltriangle.skl.utils.Common;

import static com.logicaltriangle.skl.CategoryDetailsActivity.lLNumberRange;
import static com.logicaltriangle.skl.CategoryDetailsActivity.viewPager;

public class CmnItemFragment extends Fragment implements View.OnClickListener{
    private int pos = ItemListAdapter.position;

    private Button topBackArrow,topRightSound;
    private ImageView centerIv;

    private Common common;

    String catName = "";
    int itemId = 0;
    String itemImgUrl = "";
    String itemAudioUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        topBackArrow = view.findViewById(R.id.topBackArrowId);
        topRightSound = view.findViewById(R.id.topRightSoundId);
        centerIv = view.findViewById(R.id.centerIvId);

        topBackArrow.setOnClickListener(this::onClick);
        topRightSound.setOnClickListener(this::onClick);
        centerIv.setOnClickListener(this::onClick);

        centerIv.setImageResource(R.drawable.z);

        //initiating common
        common = new Common(getActivity());
        catName = getArguments().getString("catName");
        itemId = getArguments().getInt("ItemID");
        itemImgUrl = getArguments().getString("ItemImgUrl");
        itemAudioUrl = getArguments().getString("itemAudioUrl");

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
        if (fileName != ""){
            if (fileName.contains("/"))
                fileName.replace("/", "");
            try {
                String filePath = MyApp.getInstance().filesDir + fileName;
                File file = new File(filePath);
                if (file.isFile()){
                    Glide.with(getActivity())
                            .load(Uri.fromFile(file))
                            .into(centerIv);
                }else {
                    Glide.with(getActivity())
                            .load( Uri.parse(imgUrl) )
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
            case R.id.topBackArrowId:
                if (catName.contains("Numbers")){
                    CategoryDetailsActivity.releaseMedia();
                    viewPager.setVisibility(View.GONE);
                    lLNumberRange.setVisibility(View.VISIBLE);
                } else {
                    getActivity().finish();
                }
                CategoryDetailsActivity.runningFileName = "";
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
        }
    }
}
