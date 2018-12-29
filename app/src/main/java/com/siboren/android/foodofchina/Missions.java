package com.siboren.android.foodofchina;

import java.util.Date;
import java.util.UUID;

public class Missions {

    public static final String TABLE="mission";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mNeedFood;
    private String mAward;
    private double mDistance;
    private boolean mSolved;

    public static final String KEY_mId="mId";
    public static final String KEY_mTitle="mTitle";
    public static final String KEY_mDate="mDate";
    public static final String KEY_mNeedFood="mNeedFood";
    public static final String KEY_mAward="mAward";
    public static final String KEY_mDistance="mDistance";
    public static final String KEY_mSolved="mSolved";

    public Missions(){
        //生成唯一ID
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getNeedFood() {
        return mNeedFood;
    }

    public void setNeedFood(String mNeedFood) {
        this.mNeedFood = mNeedFood;
    }

    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title){
        mTitle=title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        this.mSolved = solved;
    }

    public String getAward() {
        return mAward;
    }

    public void setAward(String mAward) {
        this.mAward = mAward;
    }

    public String getDistance() {
        return mDistance+" km";
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }
}
