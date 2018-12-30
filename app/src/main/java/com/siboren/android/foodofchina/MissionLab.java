package com.siboren.android.foodofchina;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.siboren.android.foodofchina.database.MissionBaseHelper;

public class MissionLab {
    private static MissionLab sMissionLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MissionLab get(Context context){
        if (sMissionLab == null){
            sMissionLab = new MissionLab(context);
        }
        return sMissionLab;
    }

    private MissionLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MissionBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Mission> getMissions(){
        return new ArrayList<>();
    }

    public Mission getMission(UUID id){

        return null;
    }

    public void addMission(Mission mission)
    {

    }

    public int getSize(){ return 1;}
}
