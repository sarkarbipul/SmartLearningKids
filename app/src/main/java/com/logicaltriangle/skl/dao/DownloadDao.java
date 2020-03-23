package com.logicaltriangle.skl.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.logicaltriangle.skl.model.Download;

@Dao
public interface DownloadDao {
    @Query("SELECT * FROM Download")
    public List<Download> getAllDownloads();

    @Query("SELECT * FROM Download where file_url=:fileUrl LIMIT 1")
    public Download getDownloadByUrl(String fileUrl);

    @Query("SELECT * FROM Download where id>:id LIMIT 1")
    public Download getDownloadByRowId(int id);

    @Insert
    public void insert(Download download);

    @Update
    public void update(Download download);
}
