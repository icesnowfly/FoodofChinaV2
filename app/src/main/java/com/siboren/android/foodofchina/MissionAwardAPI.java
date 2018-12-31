package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MissionAwardAPI {
    private DBHelper dbHelper;
    public MissionAwardAPI(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(MissionAward missionAward){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(MissionAward.KEY_title,missionAward.title);
        values.put(MissionAward.KEY_item_name,missionAward.item_name);
        values.put(MissionAward.KEY_num,missionAward.num);
        //
        Log.d("api_", "insert: "+values );
        long MissionAward_Id=db.insert("missionAward",null,values);
        db.close();
        return (int)MissionAward_Id;
    }

    public void delete(MissionAward missionAward){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(MissionAward.TABLE,MissionAward.KEY_title+"=?", new String[]{missionAward.title});
        db.close();
    }
    public void update(MissionAward missionAward){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(MissionAward.KEY_title,missionAward.title);
        values.put(MissionAward.KEY_item_name,missionAward.item_name);
        values.put(MissionAward.KEY_num,missionAward.num);

        db.update(MissionAward.TABLE,values,MissionAward.KEY_title+"=?", new String[]{missionAward.title});
        db.close();
    }
    public Item get_award(MissionAward missionAward){//返回奖励物品
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT *"
                +" FROM " + MissionAward.TABLE
                + " WHERE " +
                MissionAward.KEY_title+"=?";
        Item item=new Item();
        int i=0;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{missionAward.title});
        if(cursor.moveToFirst()){
            do{
                item.name[i]=cursor.getString(cursor.getColumnIndex(MissionAward.KEY_item_name));
                item.num[i]=cursor.getInt(cursor.getColumnIndex(MissionAward.KEY_num));
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return item;
    }
}
