package com.ait.kim.pantryrescue.network;

import com.ait.kim.pantryrescue.GetData.GetResult;
import com.ait.kim.pantryrescue.SearchData.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kimpham on 12/10/17.
 */

public interface RecipeApi {
    @GET("/api/search")
    Call<SearchResult> getNames(
            @Query("key") String appId, @Query("q") String name,
            @Query("sort") String sort);

    @GET("/api/get")
    Call<GetResult> getRecipe(
            @Query("key") String appId,
            @Query("rId") String recipeId);
}
