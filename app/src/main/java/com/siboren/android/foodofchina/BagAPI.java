package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class BagAPI {
    private DBHelper dbHelper;
    public BagAPI(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(Bag bag){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Bag.KEY_uid,bag.uid);
        values.put(Bag.KEY_item_name,bag.item_name);
        values.put(Bag.KEY_num,bag.num);
        //
        Log.d(TAG, "insert: "+values );
        long User_Id=db.insert(Bag.TABLE,null,values);
        db.close();
        return (int)User_Id;
    }

    public void delete(int User_Id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Bag.TABLE,Bag.KEY_uid+"=?", new String[]{String.valueOf(User_Id)});
        db.close();
    }
    public void update(Bag bag){//增减物品
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        String selectQuery="SELECT *"
                +" FROM " + Bag.TABLE
                + " WHERE " +
                Bag.KEY_uid + "=?"+" AND "
                +Bag.KEY_item_name+"=?";
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(bag.uid),bag.item_name});
        cursor.moveToFirst();
        int num=cursor.getInt(cursor.getColumnIndex(Bag.KEY_num));
        bag.num+=num;
        cursor.close();

        values.put(Bag.KEY_uid,bag.uid);
        values.put(Bag.KEY_item_name,bag.item_name);
        values.put(Bag.KEY_num,bag.num);

        db.update(User.TABLE,values,Bag.KEY_uid+"=?"+"and"+Bag.KEY_item_name+"=?",new String[] { String.valueOf(bag.uid),bag.item_name });
        db.close();
    }
    public int get_num(Bag bag){//获取该物品数目
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        String selectQuery="SELECT *"
                +" FROM " + Bag.TABLE
                + " WHERE " +
                Bag.KEY_uid + "=?"+" AND "
                +Bag.KEY_item_name+"=?";
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(bag.uid),bag.item_name});
        cursor.moveToFirst();
        int num=cursor.getInt(cursor.getColumnIndex(Bag.KEY_num));
        return num;
    }
    public void get_reward(String uid,Item item){
        int i=0;
        Bag bag_temp=new Bag(uid);
        while (item.num[i]!=0){
            bag_temp.item_name=item.name[i];
            bag_temp.num=item.num[i];
            this.update(bag_temp);
        }
    }
}
