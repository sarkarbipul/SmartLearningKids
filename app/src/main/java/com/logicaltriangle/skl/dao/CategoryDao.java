package com.logicaltriangle.skl.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.logicaltriangle.skl.model.Category;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    public List<Category> getAllCategory();

    @Insert
    public void insert(Category category);

    @Update
    public void update(Category category);

    @Query("DELETE FROM category where cat_name LIKE :param")
    public void deleteLikeCats(String param);
}
