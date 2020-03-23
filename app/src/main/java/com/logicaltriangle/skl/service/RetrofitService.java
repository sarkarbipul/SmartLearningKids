package com.logicaltriangle.skl.service;

import com.logicaltriangle.skl.model.CatResult;
import com.logicaltriangle.skl.model.ItemResult;
import com.logicaltriangle.skl.model.WordResult;
import retrofit2.Call;
import retrofit2.http.GET;

//?apiKey=1999acf8f5b5b7778558b3b0093b436b
public interface RetrofitService {
    @GET("get_category_data.php?apiKey=1999acf8f5b5b7778558b3b0093b436b")
    Call<CatResult> getCatResult();

    @GET("get_items_data.php?apiKey=1999acf8f5b5b7778558b3b0093b436b")
    Call<ItemResult> getItemResult();

    @GET("get_words_data.php?apiKey=1999acf8f5b5b7778558b3b0093b436b")
    Call<WordResult> getWordResult();
}
