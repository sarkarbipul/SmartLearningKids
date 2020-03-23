package com.logicaltriangle.skl.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.logicaltriangle.skl.model.Item;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    public List<Item> getAllItems();

    @Query("SELECT * FROM item where cat_id=:catID")
    public List<Item> getItemsByCatId(int catID);

    @Query("SELECT * FROM item where cat_id=:catID AND CAST(item_name as INT)>=:from AND CAST(item_name as INT)<=:to ORDER BY CAST(item_name as INT) ASC")
    public List<Item> getItemsByFromTo(int catID, String from, String to);

    @Query("SELECT * FROM item where item_id=:itemId")
    public Item getItemById(int itemId);

    @Insert
    public void insert(Item item);

    @Update
    public void update(Item item);
}
