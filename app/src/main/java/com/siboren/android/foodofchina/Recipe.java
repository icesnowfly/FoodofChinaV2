package com.siboren.android.foodofchina;

import java.util.Date;
import java.util.UUID;

public class Recipe {
    private UUID mId;
    private String mTitle;
    private int mNum;

    public Recipe(){
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
