package com.siboren.android.foodofchina.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.siboren.android.foodofchina.Recipe;
import com.siboren.android.foodofchina.database.RecipeDbSchema.RecipeTable;

import java.util.UUID;

public class RecipeCursorWrapper extends CursorWrapper {
    public RecipeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Recipe getRecipe(){
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        int num = getInt(getColumnIndex(RecipeTable.Cols.NUM));
        String needmaterial = getString(getColumnIndex(RecipeTable.Cols.NEEDMATERIAL));

        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setNum(num);
        recipe.setNeedMaterial(needmaterial);
        return recipe;
    }
}
