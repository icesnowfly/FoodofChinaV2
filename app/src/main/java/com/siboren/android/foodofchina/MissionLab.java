package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.siboren.android.foodofchina.database.MissionBaseHelper;
import com.siboren.android.foodofchina.database.MissionCursorWrapper;
import com.siboren.android.foodofchina.database.MissionDbSchema;
import com.siboren.android.foodofchina.database.MissionDbSchema.AwardTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.BagTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.MissionTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.RecipeTable;

public class MissionLab {
    private static MissionLab sMissionLab;
    private static final String allMaterial="所有食材";
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
        List<Mission> missions = new ArrayList<>();

        MissionCursorWrapper cursor = queryMissions(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                missions.add(cursor.getMission());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return missions;
    }

    public Mission getMission(UUID id){
        MissionCursorWrapper cursor = queryMissions(
                MissionTable.Cols.UUID+"=?",
                new String[]{id.toString()}
        );
        try{
            if (cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getMission();
        }finally {
            cursor.close();
        }
    }

    public String getMissionAward(UUID id) {
        String s="";
        MissionCursorWrapper cursor = queryAwards(
                AwardTable.Cols.MISSIONID+"=?",
                new String[]{id.toString()}
        );
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                s=s+cursor.getString(cursor.getColumnIndex(AwardTable.Cols.TITLE))+" * "+
                        cursor.getString(cursor.getColumnIndex(AwardTable.Cols.NUM));
                if (!cursor.isLast()) s=s+"\n";
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return s;
    }

    private static ContentValues getContentValues(Mission mission){
        ContentValues values = new ContentValues();
        values.put(MissionTable.Cols.UUID,mission.getId().toString());
        values.put(MissionTable.Cols.TITLE,mission.getTitle());
        values.put(MissionTable.Cols.DISTANCE,mission.getDistanceValue());
        values.put(MissionTable.Cols.NEEDFOOD,mission.getNeedFood());
        values.put(MissionTable.Cols.ACCEPTED,mission.isAccepted()?1:0);
        values.put(MissionTable.Cols.SOLVED,mission.isSolved()?1:0);
        values.put(MissionTable.Cols.LATITUDE,mission.getLocation().latitude);
        values.put(MissionTable.Cols.LONGITUDE,mission.getLocation().longitude);
        return values;
    }

    public void updateMission(Mission mission){
        String uuidString = mission.getId().toString();
        ContentValues values = getContentValues(mission);
        mDatabase.update(MissionTable.NAME,values,
                MissionTable.Cols.UUID+"=?",
                new String[]{uuidString});
    }

    public void updateRecipe(String title){
        MissionCursorWrapper cursor = queryRecipes(
                RecipeTable.Cols.TITLE+"=?",
                new String[]{title}
        );
        cursor.moveToFirst();
        try {
            Recipe recipe = cursor.getRecipe();
            ContentValues values = new ContentValues();
            values.put(RecipeTable.Cols.UUID, recipe.getId().toString());
            values.put(RecipeTable.Cols.TITLE, recipe.getTitle());
            values.put(RecipeTable.Cols.NEEDMATERIAL, recipe.getNeedMaterial());
            values.put(RecipeTable.Cols.NUM, recipe.getNum()-1);
            mDatabase.update(RecipeTable.NAME, values,
                    RecipeTable.Cols.UUID + "=?",
                    new String[]{recipe.getId().toString()});
        }finally {
            cursor.close();
        }
    }

    public void updateBag(String title, int num){
        ContentValues values = new ContentValues();
        MissionCursorWrapper cursor=queryBag(BagTable.Cols.TITLE+"=?",
                new String[]{title});
        cursor.moveToFirst();
        values.put(BagTable.Cols.TITLE,cursor.getString(cursor.getColumnIndex(BagTable.Cols.TITLE)));
        values.put(BagTable.Cols.NUM,cursor.getInt(cursor.getColumnIndex(BagTable.Cols.NUM))+num);
        mDatabase.update(BagTable.NAME,values,
                BagTable.Cols.TITLE+"=?",
                new String[]{title});
    }

    private void addAwards(Mission mission){
        ContentValues values = new ContentValues();
        values.put(AwardTable.Cols.MISSIONID,mission.getId().toString());
        values.put(AwardTable.Cols.TITLE,"经验值");
        values.put(AwardTable.Cols.NUM,Math.round(mission.getDistanceValue()/100)*10);
        mDatabase.insert(AwardTable.NAME,null,values);
        values = new ContentValues();
        values.put(AwardTable.Cols.MISSIONID,mission.getId().toString());
        values.put(AwardTable.Cols.TITLE,"元宝");
        values.put(AwardTable.Cols.NUM,Math.round(mission.getDistanceValue()/50)*2);
        mDatabase.insert(AwardTable.NAME,null,values);
        values = new ContentValues();
        values.put(AwardTable.Cols.MISSIONID,mission.getId().toString());
        values.put(AwardTable.Cols.TITLE,allMaterial);
        values.put(AwardTable.Cols.NUM,Math.round(mission.getDistanceValue()/100)*5);
        mDatabase.insert(AwardTable.NAME,null,values);
    }

    public void addMission(Mission mission)
    {
        ContentValues values = getContentValues(mission);
        MissionCursorWrapper cursor = queryRecipes(null,null);
        try{
            int n= cursor.getCount();
            int r = (int)((Math.random()*n)+1);
            n=0;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                n++;
                if (n==r){
                    values.put(MissionTable.Cols.NEEDFOOD, cursor.getRecipe().getTitle());
                    break;
                }
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        mDatabase.insert(MissionTable.NAME,null,values);
        addAwards(mission);
    }

    private MissionCursorWrapper queryMissions(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                MissionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MissionCursorWrapper(cursor);
    }

    private MissionCursorWrapper queryAwards(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                AwardTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MissionCursorWrapper(cursor);
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

    public int getSize(){
        MissionCursorWrapper cursor = queryMissions(null,null);
        return cursor.getCount();
    }

    public boolean checkMission(Mission mission){
        MissionCursorWrapper cursor = queryRecipes(
                RecipeTable.Cols.TITLE+"=?",
                new String[]{mission.getNeedFood()}
        );
        cursor.moveToFirst();
        int num=cursor.getInt(cursor.getColumnIndex(RecipeTable.Cols.NUM));
        if (num>0) return true;
        else return false;
    }

    public void completeMission(Mission mission) {
        MissionCursorWrapper cursor = queryAwards(
                AwardTable.Cols.MISSIONID+"=? and "+
                        AwardTable.Cols.TITLE+"=?",
                new String[]{mission.getId().toString(),allMaterial}
        );
        try{
            cursor.moveToFirst();
            int num=cursor.getInt(cursor.getColumnIndex(AwardTable.Cols.NUM));
            String title;
            MissionCursorWrapper c = queryBag(null,null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                title=c.getString(c.getColumnIndex(BagTable.Cols.TITLE));
                updateBag(title,num);
                c.moveToNext();
            }
            c.close();
        }finally {
            cursor.close();
        }
        updateRecipe(mission.getNeedFood());
    }

    public void deleteMission(UUID id){
        mDatabase.delete(MissionTable.NAME,MissionTable.Cols.UUID+"=?",
                new String[]{id.toString()});
    }
}
