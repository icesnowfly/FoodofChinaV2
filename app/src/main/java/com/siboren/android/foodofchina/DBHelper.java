package com.siboren.android.foodofchina;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    //每次你对数据表进行编辑，添加时候，你都需要对数据库的版本进行提升

    //数据库版本
    private static final int DATABASE_VERSION=3;

    //数据库名称
    private static final String DATABASE_NAME="test.db";


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        SQLiteDatabase db=getWritableDatabase();
        onCreate(db);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        Log.d("Helper_", "onCreate: ");
        //User表
        String CREATE_TABLE_User="CREATE TABLE if not exists "+ User.TABLE+"("
                +User.KEY_ID+" TEXT PRIMARY KEY,"
                +User.KEY_name+" TEXT, "
                +User.KEY_password+" TEXT, "
                +User.KEY_gender+" INT,"
                +User.KEY_exp+" INT,"
                +User.KEY_level+" INT)";
        Log.d("Helper_", "onCreate: "+CREATE_TABLE_User );
        db.execSQL(CREATE_TABLE_User);
        //Mission表
        String CREATE_TABLE_Mission="CREATE TABLE if not exists "+ Mission.TABLE+"("
                +Mission.KEY_UUID+" INT PRIMARY KEY,"
                +Mission.KEY_mTitle+" TEXT, "
                +Mission.KEY_mDate+" Data, "
                +Mission.KEY_mNeedFood+" TEXT,"
                +Mission.KEY_mAward+" TEXT,"
                +Mission.KEY_mDistance+" DOUBLE,"
                +Mission.KEY_mSolved+" INT)";
        Log.d("Helper_", "onCreate: "+CREATE_TABLE_Mission );
        db.execSQL(CREATE_TABLE_Mission);
        //Recipe表
        String CREATE_TABLE_Recipe="CREATE TABLE if not exists "+ Recipe.TABLE+"("
                +Recipe.KEY_mId+" INT PRIMARY KEY,"
                +Recipe.KEY_mTitle+" TEXT, "
                +Recipe.KEY_mNum+" INT)";
        Log.d("Helper_", "onCreate: "+CREATE_TABLE_Recipe );
        db.execSQL(CREATE_TABLE_Recipe);
        //Bag表
        String CREATE_TABLE_Bag="CREATE TABLE if not exists "+ Bag.TABLE+"("
                +Bag.KEY_uid+" INT,"
                +Bag.KEY_item_name+" TEXT, "
                +Bag.KEY_num+" INT)";
        Log.d("Helper_", "onCreate: "+CREATE_TABLE_Bag );
        db.execSQL(CREATE_TABLE_Bag);
        //CookBook表
        String CREATE_TABLE_CookBook="CREATE TABLE if not exists "+ CookBook.TABLE+"("
                +CookBook.KEY_title+" TEXT PRIMARY KEY,"
                +CookBook.KEY_material+" TEXT, "
                +CookBook.KEY_num+" INT)";
        Log.d("Helper_", "onCreate: "+CREATE_TABLE_CookBook );
        db.execSQL(CREATE_TABLE_CookBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+ User.TABLE);

        //再次创建表
        onCreate(db);
    }
}