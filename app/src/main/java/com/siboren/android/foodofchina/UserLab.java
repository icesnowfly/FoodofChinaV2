package com.siboren.android.foodofchina;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.siboren.android.foodofchina.database.MissionBaseHelper;
import com.siboren.android.foodofchina.database.MissionCursorWrapper;
import com.siboren.android.foodofchina.database.MissionDbSchema;
import com.siboren.android.foodofchina.database.MissionDbSchema.UserTable;

public class UserLab {
    private static UserLab sUserLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static UserLab get(Context context) {
        if (sUserLab == null) {
            sUserLab = new UserLab(context);
        }
        return sUserLab;
    }

    private UserLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MissionBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void InsertUser(User user){
        ContentValues values = getContentValues(user);
        mDatabase.insert(UserTable.NAME,null,values);
    }

    private static ContentValues getContentValues(User user){
        ContentValues values = new ContentValues();
        values.put(UserTable.Cols.UUID, user.getId().toString());
        values.put(UserTable.Cols.ACCOUNT, user.getAccount());
        values.put(UserTable.Cols.PASSWORD,user.getPassword());
        values.put(UserTable.Cols.LEVEL, user.getLevel());
        values.put(UserTable.Cols.EXP, user.getExp());
        values.put(UserTable.Cols.MONEY, user.getMoney());

        return values;
    }

    public boolean isUserExist(String account){
         MissionCursorWrapper cursor = queryUsers(
                UserTable.Cols.ACCOUNT+"=?",
                new String[]{account}
         );
         if (cursor.getCount()==0) return true;
         else return false;
    }

    private MissionCursorWrapper queryUsers(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                UserTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MissionCursorWrapper(cursor);
    }

    public int checkLogin(String account, String password){
        MissionCursorWrapper cursor = queryUsers(
                UserTable.Cols.ACCOUNT+"=?",
                new String[]{account}
        );
        if (cursor.getCount()==0) return 0;
        cursor.moveToFirst();
        User user = cursor.getUser();
        if (user.getPassword().equals(password))
            return 1;
        else return 2;
    }
}