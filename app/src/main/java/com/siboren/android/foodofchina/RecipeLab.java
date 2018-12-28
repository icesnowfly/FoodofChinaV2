package com.siboren.android.foodofchina;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeLab {
    private static RecipeLab sRecipeLab;
    private List<Recipe> mRecipes;

    public static RecipeLab get(Context context){
        if (sRecipeLab == null){
            sRecipeLab = new RecipeLab(context);
        }
        return sRecipeLab;
    }

    private RecipeLab(Context context){
        mRecipes = new ArrayList<>();
        for (int i=0;i<100;i++){
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe #"+i);
            recipe.setNum(i%3);
            mRecipes.add(recipe);
        }
    }

    public List<Recipe> getRecipes(){
        return mRecipes;
    }

    public Recipe getRecipe(UUID id){
        for (Recipe recipe:mRecipes){
            if (recipe.getId().equals(id)){
                return recipe;
            }
        }
        return null;
    }
}
