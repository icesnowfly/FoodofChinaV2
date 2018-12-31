package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MissionAPI {
    private DBHelper dbHelper;
    public Context mcontext;

    public MissionAPI(Context context){
        dbHelper=new DBHelper(context);
        mcontext=context;
    }

    public int insert(Mission mission){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        
        values.put(Mission.KEY_mId, String.valueOf(mission.getId()));
        values.put(Mission.KEY_mTitle,mission.getTitle());
        values.put(Mission.KEY_mDate, String.valueOf(mission.getDate()));
        values.put(Mission.KEY_mNeedFood, mission.getNeedFood());
        values.put(Mission.KEY_mAward, mission.getAward());
        values.put(Mission.KEY_mDistance, mission.getDistance());
        values.put(Mission.KEY_mSolved, mission.isSolved());
        //
        Log.d("api_", "insert: "+values );
        long Mission_Id=db.insert("mission",null,values);
        db.close();
        return (int)Mission_Id;
    }

    public void delete(Mission mission){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Mission.TABLE,Mission.KEY_mId+"=?", new String[]{String.valueOf(mission.getId())});
        db.close();
    }
    public void update(Mission mission){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Mission.KEY_mId, String.valueOf(mission.getId()));
        values.put(Mission.KEY_mTitle,mission.getTitle());
        values.put(Mission.KEY_mDate, String.valueOf(mission.getDate()));
        values.put(Mission.KEY_mNeedFood, mission.getNeedFood());
        values.put(Mission.KEY_mAward, mission.getAward());
        values.put(Mission.KEY_mDistance, mission.getDistance());
        values.put(Mission.KEY_mSolved, mission.isSolved());

        db.update(Mission.TABLE,values,Mission.KEY_mId+"=?", new String[]{String.valueOf(mission.getId())});
        db.close();
    }
    public Item give_award(String uid,String title){
        Item item=new Item();
        MissionAwardAPI missionAwardAPI=new MissionAwardAPI(mcontext);
        MissionAward missionAward=new MissionAward(title);
        BagAPI bagAPI=new BagAPI(mcontext);

        item=missionAwardAPI.get_award(missionAward);//奖励物资
        bagAPI.get_reward(uid,item);//奖励物资置入背包

        return item;
    }
}
