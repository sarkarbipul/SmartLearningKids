package com.logicaltriangle.skl.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.logicaltriangle.skl.dao.CategoryDao;
import com.logicaltriangle.skl.dao.DownloadDao;
import com.logicaltriangle.skl.dao.ItemDao;
import com.logicaltriangle.skl.dao.WordDao;
import com.logicaltriangle.skl.model.Category;
import com.logicaltriangle.skl.model.Download;
import com.logicaltriangle.skl.model.Item;
import com.logicaltriangle.skl.model.Word;

@Database(entities = {Category.class, Item.class, Word.class, Download.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao getCategoryDao();
    public abstract ItemDao getItemDao();
    public abstract WordDao getWordDao();
    public abstract DownloadDao getDownloadDao();

}
