package com.ait.kim.pantryrescue.GetData;


import com.ait.kim.pantryrescue.GetData.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetResult {

    @SerializedName("recipe")
    @Expose
    public Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}


