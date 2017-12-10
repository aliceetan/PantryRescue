package com.ait.kim.pantryrescue.GetData;

/**
 * Created by alicetan on 12/10/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("publisher")
    @Expose
    public String publisher;
    @SerializedName("f2f_url")
    @Expose
    public String f2fUrl;
    @SerializedName("ingredients")
    @Expose
    public List<String> ingredients = null;
    @SerializedName("source_url")
    @Expose
    public String sourceUrl;
    @SerializedName("recipe_id")
    @Expose
    public String recipeId;
    @SerializedName("image_url")
    @Expose
    public String imageUrl;
    @SerializedName("social_rank")
    @Expose
    public Double socialRank;
    @SerializedName("publisher_url")
    @Expose
    public String publisherUrl;
    @SerializedName("title")
    @Expose
    public String title;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getF2fUrl() {
        return f2fUrl;
    }

    public void setF2fUrl(String f2fUrl) {
        this.f2fUrl = f2fUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getSocialRank() {
        return socialRank;
    }

    public void setSocialRank(Double socialRank) {
        this.socialRank = socialRank;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
