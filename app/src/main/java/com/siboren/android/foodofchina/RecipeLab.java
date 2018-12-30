package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.siboren.android.foodofchina.database.RecipeBaseHelper;
import com.siboren.android.foodofchina.database.RecipeCursorWrapper;
import com.siboren.android.foodofchina.database.RecipeDbSchema;
import com.siboren.android.foodofchina.database.RecipeDbSchema.RecipeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeLab {
    private static RecipeLab sRecipeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RecipeLab get(Context context){
        if (sRecipeLab == null){
            sRecipeLab = new RecipeLab(context);
        }
        return sRecipeLab;
    }

    private RecipeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new RecipeBaseHelper(mContext)
                .getWritableDatabase();
        Recipe tempRecipe = new Recipe();
        tempRecipe.setTitle("hello");
        tempRecipe.setNeedMaterial("fish");
        addRecipe(tempRecipe);
        Recipe tempRecipe2 = new Recipe();
        tempRecipe2.setTitle("world");
        tempRecipe2.setNeedMaterial("fish");
        addRecipe(tempRecipe2);
    }

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        RecipeCursorWrapper cursor = queryRecipes(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                recipes.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return recipes;
    }

    public void addRecipe(Recipe r){
        ContentValues values = getContentValues(r);
        mDatabase.insert(RecipeTable.NAME,null,values);
    }

    public Recipe getRecipe(UUID id){
        RecipeCursorWrapper cursor=queryRecipes(
                RecipeTable.Cols.UUID+"=?",
                new String[]{id.toString()}
        );
        try{
            if (cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRecipe();
        }finally {
            cursor.close();
        }
    }

    public void updateRecipe(Recipe recipe){
        String uuidString = recipe.getId().toString();
        ContentValues values=getContentValues(recipe);
        mDatabase.update(RecipeTable.NAME,values,
                RecipeTable.Cols.UUID+"=?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Cols.UUID, recipe.getId().toString());
        values.put(RecipeTable.Cols.TITLE, recipe.getTitle());
        values.put(RecipeTable.Cols.NEEDMATERIAL,recipe.getNeedMaterial());
        values.put(RecipeTable.Cols.NUM, recipe.getNum());

        return values;
    }

    private RecipeCursorWrapper queryRecipes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                RecipeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new RecipeCursorWrapper(cursor);
    }
}
