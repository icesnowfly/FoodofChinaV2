package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MissionsAPI {
    private DBHelper dbHelper;
    public MissionsAPI(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(Missions missions){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        
        values.put(Missions.KEY_mId, String.valueOf(missions.getId()));
        values.put(Missions.KEY_mTitle,missions.getTitle());
        values.put(Missions.KEY_mDate, String.valueOf(missions.getDate()));
        values.put(Missions.KEY_mNeedFood, missions.getNeedFood());
        values.put(Missions.KEY_mAward, missions.getAward());
        values.put(Missions.KEY_mDistance, missions.getDistance());
        values.put(Missions.KEY_mSolved, missions.isSolved());
        //
        Log.d("api_", "insert: "+values );
        long Missions_Id=db.insert("missions",null,values);
        db.close();
        return (int)Missions_Id;
    }

    public void delete(Missions missions){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Missions.TABLE,Missions.KEY_mId+"=?", new String[]{String.valueOf(missions.getId())});
        db.close();
    }
    public void update(Missions missions){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Missions.KEY_mId, String.valueOf(missions.getId()));
        values.put(Missions.KEY_mTitle,missions.getTitle());
        values.put(Missions.KEY_mDate, String.valueOf(missions.getDate()));
        values.put(Missions.KEY_mNeedFood, missions.getNeedFood());
        values.put(Missions.KEY_mAward, missions.getAward());
        values.put(Missions.KEY_mDistance, missions.getDistance());
        values.put(Missions.KEY_mSolved, missions.isSolved());

        db.update(Missions.TABLE,values,Missions.KEY_mId+"=?", new String[]{String.valueOf(missions.getId())});
        db.close();
    }
}
