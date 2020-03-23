package com.logicaltriangle.skl.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.logicaltriangle.skl.model.Word;

@Dao
public interface WordDao {
    @Query("SELECT * FROM word")
    public List<Word> getAllWords();

    @Query("SELECT * FROM word where item_id=:itemID")
    public Word getWordByItemId(int itemID);

    @Insert
    public void insert(Word word);

    @Update
    public void update(Word word);
}
