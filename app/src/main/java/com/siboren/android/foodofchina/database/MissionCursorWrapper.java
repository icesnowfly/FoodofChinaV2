package com.siboren.android.foodofchina.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.baidu.mapapi.model.LatLng;
import com.siboren.android.foodofchina.Mission;
import com.siboren.android.foodofchina.Recipe;
import com.siboren.android.foodofchina.User;
import com.siboren.android.foodofchina.database.MissionDbSchema.MissionTable;
import com.siboren.android.foodofchina.database.MissionDbSchema.RecipeTable;

import java.util.UUID;

public class MissionCursorWrapper extends CursorWrapper {
    public MissionCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Mission getMission(){
        String uuidString = getString(getColumnIndex(MissionTable.Cols.UUID));
        String title = getString(getColumnIndex(MissionTable.Cols.TITLE));
        String needFood = getString(getColumnIndex(MissionTable.Cols.NEEDFOOD));
        double distance = getDouble(getColumnIndex(MissionTable.Cols.DISTANCE));
        int isAccepted = getInt(getColumnIndex(MissionTable.Cols.ACCEPTED));
        int isSolved = getInt(getColumnIndex(MissionTable.Cols.SOLVED));
        double latitude = getDouble(getColumnIndex(MissionTable.Cols.LATITUDE));
        double longitude = getDouble(getColumnIndex(MissionTable.Cols.LONGITUDE));

        Mission mission = new Mission(UUID.fromString(uuidString));
        mission.setTitle(title);
        mission.setDistance(distance);
        mission.setNeedFood(needFood);
        mission.setAccepted(isAccepted !=0);
        mission.setSolved(isSolved!=0);
        mission.setLocation(new LatLng(latitude,longitude));

        return mission;
    }

    public Recipe getRecipe(){
        String uuidString = getString(getColumnIndex(RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        int num = getInt(getColumnIndex(RecipeTable.Cols.NUM));
        String needMaterial = getString(getColumnIndex(RecipeTable.Cols.NEEDMATERIAL));
        Recipe recipe = new Recipe(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setNum(num);
        recipe.setNeedMaterial(needMaterial);
        return recipe;
    }

    public User getUser(){
        String uuidString = getString(getColumnIndex(MissionDbSchema.UserTable.Cols.UUID));
        String account = getString(getColumnIndex(MissionDbSchema.UserTable.Cols.ACCOUNT));
        String password = getString(getColumnIndex(MissionDbSchema.UserTable.Cols.PASSWORD));
        int level = getInt(getColumnIndex(MissionDbSchema.UserTable.Cols.LEVEL));
        int exp = getInt(getColumnIndex(MissionDbSchema.UserTable.Cols.EXP));
        int money = getInt(getColumnIndex(MissionDbSchema.UserTable.Cols.MONEY));
        User user = new User(UUID.fromString(uuidString));
        user.setAccount(account);
        user.setPassword(password);
        user.setLevel(level);
        user.setExp(exp);
        user.setMoney(money);
        return user;
    }
}
