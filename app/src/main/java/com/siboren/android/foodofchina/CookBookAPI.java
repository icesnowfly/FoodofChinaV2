package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CookBookAPI {
    private DBHelper dbHelper;
    public CookBookAPI(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(CookBook cookBook){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CookBook.KEY_title,cookBook.title);
        values.put(CookBook.KEY_material,cookBook.material);
        values.put(CookBook.KEY_num,cookBook.num);
        //
        Log.d("api_", "insert: "+values );
        long CookBook_Id=db.insert("cookBook",null,values);
        db.close();
        return (int)CookBook_Id;
    }

    public void delete(CookBook cookBook){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(CookBook.TABLE,CookBook.KEY_title+"=?", new String[]{cookBook.title});
        db.close();
    }
    public void update(CookBook cookBook){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(CookBook.KEY_title,cookBook.title);
        values.put(CookBook.KEY_material,cookBook.material);
        values.put(CookBook.KEY_num,cookBook.num);

        db.update(CookBook.TABLE,values,CookBook.KEY_title+"=?", new String[]{cookBook.title});
        db.close();
    }
    public Item get_cookbooks(CookBook cookBook){//返回所需材料
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT *"
                +" FROM " + CookBook.TABLE
                + " WHERE " +
                CookBook.KEY_title+"=?";
        Item item=new Item();
        int i=0;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{cookBook.title});
        if(cursor.moveToFirst()){
            do{
                item.name[i]=cursor.getString(cursor.getColumnIndex(CookBook.KEY_material));
                item.num[i]=cursor.getInt(cursor.getColumnIndex(CookBook.KEY_num));
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return item;
    }
}
