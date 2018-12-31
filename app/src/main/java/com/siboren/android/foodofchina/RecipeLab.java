package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.siboren.android.foodofchina.database.MissionBaseHelper;
import com.siboren.android.foodofchina.database.MissionCursorWrapper;
import com.siboren.android.foodofchina.database.MissionDbSchema;
import com.siboren.android.foodofchina.database.MissionDbSchema.BagTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.MaterialTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.RecipeTable;

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
        mDatabase = new MissionBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Recipe> getRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        MissionCursorWrapper cursor = queryRecipes(null,null);

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

    public void addMaterial(UUID id,String title,int num){
        ContentValues values = new ContentValues();
        values.put(MaterialTable.Cols.RECIPEID,id.toString());
        values.put(MaterialTable.Cols.TITLE,title);
        values.put(MaterialTable.Cols.NUM, String.valueOf(num));
        mDatabase.insert(MaterialTable.NAME,null,values);
    }

    public void addRecipe(Recipe r){
        ContentValues values = getContentValues(r);
        mDatabase.insert(RecipeTable.NAME,null,values);
    }

    public void insertBag(String title, int num){
        ContentValues values = new ContentValues();
        values.put(BagTable.Cols.TITLE,title);
        values.put(BagTable.Cols.NUM,String.valueOf(num));
        mDatabase.insert(BagTable.NAME,null,values);
    }

    public Recipe getRecipe(UUID id){
        MissionCursorWrapper cursor=queryRecipes(
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

    public void updateBag(String title, int num){
        ContentValues values = new ContentValues();
        MissionCursorWrapper cursor=queryBag(BagTable.Cols.TITLE+"=?",
                new String[]{title});
        cursor.moveToFirst();
        values.put(BagTable.Cols.TITLE,cursor.getString(cursor.getColumnIndex(BagTable.Cols.TITLE)));
        values.put(BagTable.Cols.NUM,cursor.getInt(cursor.getColumnIndex(BagTable.Cols.NUM))-num);
        mDatabase.update(BagTable.NAME,values,
                BagTable.Cols.TITLE+"=?",
                new String[]{title});
    }

    private static ContentValues getContentValues(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Cols.UUID, recipe.getId().toString());
        values.put(RecipeTable.Cols.TITLE, recipe.getTitle());
        values.put(RecipeTable.Cols.NEEDMATERIAL,recipe.getNeedMaterial());
        values.put(RecipeTable.Cols.NUM, recipe.getNum());

        return values;
    }

    public String getRecipeMaterial(UUID id) {
        String s="";
        MissionCursorWrapper cursor = queryMaterial(
                MaterialTable.Cols.RECIPEID+"=?",
                new String[]{id.toString()}
        );
        try{
            cursor.moveToFirst();
            String title;
            int recNum,haveNum;
            MissionCursorWrapper c;
            while(!cursor.isAfterLast())
            {
                title = cursor.getString(cursor.getColumnIndex(MaterialTable.Cols.TITLE));
                c = queryBag(BagTable.Cols.TITLE+"=?",new String[]{title});
                c.moveToFirst();
                recNum = cursor.getInt(cursor.getColumnIndex(MaterialTable.Cols.NUM));
                haveNum = c.getInt(c.getColumnIndex(BagTable.Cols.NUM));
                s=s+title+" "+ String.valueOf(haveNum)+"/"+String.valueOf(recNum);
                if (!cursor.isLast()) s=s+"\n";
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return s;
    }

    private MissionCursorWrapper queryRecipes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                RecipeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MissionCursorWrapper(cursor);
    }

    private MissionCursorWrapper queryMaterial(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                MaterialTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MissionCursorWrapper(cursor);
    }

    private MissionCursorWrapper queryBag(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                BagTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MissionCursorWrapper(cursor);
    }

    public boolean checkRecipe(Recipe recipe){
        MissionCursorWrapper cursor = queryMaterial(
                MaterialTable.Cols.RECIPEID+"=?",
                new String[]{recipe.getId().toString()}
        );
        boolean isAvailable=true;
        try{
            cursor.moveToFirst();
            String title;
            int recNum;
            int haveNum;
            MissionCursorWrapper c;
            while(!cursor.isAfterLast()){
                title=cursor.getString(cursor.getColumnIndex(MaterialTable.Cols.TITLE));
                recNum=cursor.getInt(cursor.getColumnIndex(MaterialTable.Cols.NUM));
                c = queryBag(
                        BagTable.Cols.TITLE+"=?",
                        new String[]{title}
                );
                c.moveToFirst();
                haveNum=c.getInt(c.getColumnIndex(BagTable.Cols.NUM));
                if (haveNum<recNum){
                    isAvailable=false;
                    break;
                }
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return isAvailable;
    }

    public void compoundRecipe(Recipe recipe){
        MissionCursorWrapper cursor = queryMaterial(
                MaterialTable.Cols.RECIPEID+"=?",
                new String[]{recipe.getId().toString()}
        );
        try{
            cursor.moveToFirst();
            String title;
            int num;
            while(!cursor.isAfterLast()){
                title=cursor.getString(cursor.getColumnIndex(MaterialTable.Cols.TITLE));
                num=cursor.getInt(cursor.getColumnIndex(MaterialTable.Cols.NUM));
                updateBag(title,num);
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
    }
}
