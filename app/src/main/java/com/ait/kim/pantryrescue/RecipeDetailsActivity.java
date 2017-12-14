package com.ait.kim.pantryrescue;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.kim.pantryrescue.GetData.GetResult;
import com.ait.kim.pantryrescue.R;
import com.ait.kim.pantryrescue.SearchData.SearchResult;
import com.ait.kim.pantryrescue.data.Post;
import com.ait.kim.pantryrescue.network.RecipeApi;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final int REQUEST_NEW_POST = 101 ;
    public static final String RECIPE_TITLE = "RECIPE_TITLE";
    public static final String IMAGE_URL = "IMAGE_URL";
    private String recipeId;
    public String appid = "7f1b23084710b3c5524faf0d773ee434";
    public String title;
    public String imgUrl;
    private ImageButton btnFav;
    private ImageButton btnHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://food2fork.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnFav = findViewById(R.id.btnFav);
        btnHome = findViewById(R.id.btnHome);
        favButton();
        homeButton();


        recipeId = getIntent().getStringExtra(RecipeNamesActivity.RECIPE_ID);

        final LinearLayout detailsLayout = findViewById(R.id.detailsLayout);

        final TextView tvTitle = findViewById(R.id.tvTitle);

        final TextView tvUrl = findViewById(R.id.tvUrl);

        final ImageView ivPic = findViewById(R.id.ivPic);

        final RecipeApi foodAPI = retrofit.create(RecipeApi.class);




        Call<GetResult> call = foodAPI.getRecipe(appid, recipeId);

        call.enqueue(new Callback<GetResult>() {
            @Override
            public void onResponse(Call<GetResult> call, final Response<GetResult> response) {
                if (response.body() != null) {
                    int size = response.body().getRecipe().getIngredients().size();
                    for (int i = 0; i < size; i++) {
                        TextView tvIngredient = new TextView(RecipeDetailsActivity.this);
                        tvIngredient.setTextSize(17);
                        tvIngredient.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
                        tvTitle.setText(response.body().getRecipe().getTitle());
                        title = response.body().getRecipe().getTitle();
                        tvIngredient.setText(response.body().getRecipe().getIngredients().get(i));
                        ((LinearLayout) detailsLayout).addView(tvIngredient);
                        imgUrl = response.body().getRecipe().getImageUrl();
                        Glide.with(RecipeDetailsActivity.this).load(imgUrl).into(ivPic);
//
//                        tvUrl.setText(url);

                        final String url = response.body().getRecipe().getSourceUrl();
                        tvUrl.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                //String url = "http://www.stackoverflow.com";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
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

    private void homeButton() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(RecipeDetailsActivity.this, MainActivity.class);
                startActivity(newIntent);
            }
        });
    }

    private void favButton() {
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a post item and save it to the discussions post

                showCreatePostActivity(title, imgUrl);

            }
        });
    }

    private void showCreatePostActivity(String titleID, String imgUrl) {

        Intent intentCreate = new Intent(RecipeDetailsActivity.this, CreatePostActivity.class);
        intentCreate.putExtra(RECIPE_TITLE, titleID);
        intentCreate.putExtra(IMAGE_URL, imgUrl);
        intentCreate.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentCreate);
    }
}
