package com.ait.kim.pantryrescue;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.kim.pantryrescue.GetData.GetResult;
import com.ait.kim.pantryrescue.R;
import com.ait.kim.pantryrescue.SearchData.SearchResult;
import com.ait.kim.pantryrescue.network.RecipeApi;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailsActivity extends AppCompatActivity {

    private String recipeId;
    public String appid = "7f1b23084710b3c5524faf0d773ee434";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://food2fork.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        recipeId = getIntent().getStringExtra(RecipeNamesActivity.RECIPE_ID);

        final LinearLayout detailsLayout = findViewById(R.id.detailsLayout);

        final ImageView ivPic = findViewById(R.id.ivPic);

        final RecipeApi foodAPI = retrofit.create(RecipeApi.class);


        Call<GetResult> call = foodAPI.getRecipe(appid, recipeId);

        call.enqueue(new Callback<GetResult>() {
            @Override
            public void onResponse(Call<GetResult> call, Response<GetResult> response) {
                if(response.body() != null){
                    int size = response.body().getRecipe().getIngredients().size();
                    for(int i = 0; i < size; i ++){
                        TextView tvIngredient = new TextView(RecipeDetailsActivity.this);
                        tvIngredient.setText(response.body().getRecipe().getIngredients().get(i));
                        ((LinearLayout) detailsLayout).addView(tvIngredient);
                        String imgUrl = response.body().getRecipe().getImageUrl();
                        Glide.with(RecipeDetailsActivity.this).load(imgUrl).into(ivPic);
                    }
                }

            }

            @Override
            public void onFailure(Call<GetResult> call, Throwable t) {
                TextView tvError = new TextView(RecipeDetailsActivity.this);
                tvError.setText("No ingredients to show");
            }
        });

    }
}
