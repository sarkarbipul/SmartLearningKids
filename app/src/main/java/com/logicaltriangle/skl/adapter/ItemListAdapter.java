package com.logicaltriangle.skl.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import com.logicaltriangle.skl.fragment.AlphabetFragment;
import com.logicaltriangle.skl.fragment.CmnItemFragment;
import com.logicaltriangle.skl.model.Item;


public class ItemListAdapter extends FragmentStateAdapter {
        //images length
    //int imgLength;

    public static int position;
    int categoryID;
    String catName;
    int indexRange;
    String from = "", to = "";

    List<Item> itemList;

    public ItemListAdapter(@NonNull FragmentActivity fragmentActivity, int categoryID, String catName, int indexRange, List<Item> itemList) {
        super(fragmentActivity);
        this.categoryID = categoryID;
        this.catName = catName;
        this.indexRange = indexRange;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        //List<Item> itemList = SplashActivity.appDatabase.getItemDao().getItemsByCatId(categoryID);
        //Log.d(SplashActivity.TAG, "itemsFromLocal: " + itemList.size());

        if (catName.contains("Alphabet")){

            AlphabetFragment fragment = new AlphabetFragment();
            Bundle bundle = new Bundle();
            Item item = itemList.get(position);
            bundle.putInt("ItemID", item.getItemId());
            bundle.putString("ItemImgUrl", item.getItemImgUrl());
            bundle.putString("itemAudioUrl", item.getItemAudioUrl());
            fragment.setArguments(bundle);

            return fragment;

        } else {
            CmnItemFragment fragment = new CmnItemFragment();
            Bundle bundle = new Bundle();
            Item item = itemList.get(position);
            bundle.putString("catName", catName);
            bundle.putInt("ItemID", item.getItemId());
            bundle.putString("ItemImgUrl", item.getItemImgUrl());
            bundle.putString("itemAudioUrl", item.getItemAudioUrl());
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
        //return imgLength;
    }
}
