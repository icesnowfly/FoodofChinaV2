package com.siboren.android.foodofchina;

import java.util.Date;
import java.util.UUID;

public class Recipe {
    public static final String TABLE="recipe";

    private String mId;
    private String mTitle;
    private int mNum;

    public static final String KEY_mId="mId";
    public static final String KEY_mTitle="mTitle";
    public static final String KEY_mNum="mNum";


    public Recipe(String user_name){
        mId = user_name;
        mNum = 0;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int mNum) {
        this.mNum = mNum;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
