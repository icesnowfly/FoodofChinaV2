package com.siboren.android.foodofchina.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.siboren.android.foodofchina.database.MissionDbSchema.MissionTable;

public class MissionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "missionBase.db";

    public MissionBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + MissionTable.NAME+"("+
                "_id integer primary key autoincrement, "+
        MissionTable.Cols.UUID+", "+
        MissionTable.Cols.TITLE +", "+
        MissionTable.Cols.AWARD +", "+
        MissionTable.Cols.NEEDFOOD+", "+
        MissionTable.Cols.DISTANCE+", "+
        MissionTable.Cols.ACCEPTED+", "+
        MissionTable.Cols.SOLVED+", "+
        MissionTable.Cols.DISTANCE+ ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
