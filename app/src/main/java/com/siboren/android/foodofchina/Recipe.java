package com.siboren.android.foodofchina;

import java.util.Date;
import java.util.UUID;

public class Recipe {
    public static final String TABLE="recipe";

    private UUID mId;
    public String UID;
    private String mTitle;
    private int mNum;

    public static final String KEY_UID="UID";
    public static final String KEY_mId="mId";
    public static final String KEY_mTitle="mTitle";
    public static final String KEY_mNum="mNum";


    public Recipe(){
        //生成唯一ID
        mId = UUID.randomUUID();
        mNum = 0;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int mNum) {
        this.mNum = mNum;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
