package com.siboren.android.foodofchina.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.siboren.android.foodofchina.database.MissionDbSchema.AwardTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.BagTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.MaterialTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.MissionTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.RecipeTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.UserTable;

public class MissionBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "DataBase.db";

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
        MissionTable.Cols.SOLVED +", " +
        MissionTable.Cols.LATITUDE+", "+
        MissionTable.Cols.LONGITUDE+")");

        db.execSQL("create table " + AwardTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                AwardTable.Cols.MISSIONID+", "+
                AwardTable.Cols.TITLE +", "+
                AwardTable.Cols.NUM+")"
        );

        db.execSQL("create table " + MaterialTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                MaterialTable.Cols.RECIPEID+" , "+
                MaterialTable.Cols.TITLE +", "+
                MaterialTable.Cols.NUM+")"
        );

        db.execSQL("create table " + RecipeTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                RecipeTable.Cols.UUID+", "+
                RecipeTable.Cols.TITLE +", "+
                RecipeTable.Cols.NEEDMATERIAL +", "+
                RecipeTable.Cols.NUM+")"
        );

        db.execSQL("create table " + BagTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                BagTable.Cols.TITLE +", "+
                BagTable.Cols.NUM+")"
        );

        db.execSQL("create table " + UserTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                UserTable.Cols.UUID+", "+
                UserTable.Cols.ACCOUNT+", "+
                UserTable.Cols.PASSWORD+", "+
                UserTable.Cols.LEVEL+", "+
                UserTable.Cols.EXP+", "+
                UserTable.Cols.MONEY+")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
