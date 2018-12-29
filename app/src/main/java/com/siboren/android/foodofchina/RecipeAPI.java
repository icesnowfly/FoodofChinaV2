package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecipeAPI {
    private DBHelper dbHelper;
    public Context mcontext;
    public RecipeAPI(Context context){
        dbHelper=new DBHelper(context);
        mcontext=context;
    }

    public int insert(Recipe recipe){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Recipe.KEY_mId,recipe.getId());
        values.put(Recipe.KEY_mTitle,recipe.getTitle());
        values.put(Recipe.KEY_mNum,recipe.getNum());
        //
        Log.d("api_", "insert: "+values );
        long Recipe_Id=db.insert("recipe",null,values);
        db.close();
        return (int)Recipe_Id;
    }

    public void delete(Recipe recipe){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Recipe.TABLE,Recipe.KEY_mId+"=?", new String[]{recipe.getId()});
        db.close();
    }
    public void update(Recipe recipe){//增减配方数量
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        String selectQuery="SELECT *"
                +" FROM " + Recipe.TABLE
                + " WHERE " +
                Recipe.KEY_mId + "=?"+" AND "
                +Recipe.KEY_mTitle+"=?";
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(recipe.getId()),recipe.getTitle()});
        cursor.moveToFirst();
        int num=cursor.getInt(cursor.getColumnIndex(Recipe.KEY_mNum));
        num+=recipe.getNum();
        recipe.setNum(num);
        cursor.close();
        
        values.put(Recipe.KEY_mId,recipe.getId());
        values.put(Recipe.KEY_mTitle,recipe.getTitle());
        values.put(Recipe.KEY_mNum,recipe.getNum());

        db.update(Recipe.TABLE,values,Recipe.KEY_mId+"=?", new String[]{recipe.getId()});
        db.close();
    }
    public boolean check_material(Recipe recipe){//判断材料是否足够
        Item item=new Item();//用于存储需要的材料清单
        CookBookAPI cookBookAPI=new CookBookAPI(mcontext);
        CookBook cookBook=new CookBook(recipe.getTitle());
        BagAPI bagAPI=new BagAPI(mcontext);
        Bag bag=new Bag(recipe.getId());

        item=cookBookAPI.get_cookbooks(cookBook);
        int i=0;
        while(item.num[i]!=0){
            bag.item_name=item.name[i];
            if (item.num[i]<=bagAPI.get_num(bag)){
                i++;
            }
            else {
                return false;
            }
        }
        return true;
    }
}
