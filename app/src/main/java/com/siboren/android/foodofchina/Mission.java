package com.siboren.android.foodofchina;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

import java.util.Date;
import java.util.UUID;

public class Mission implements Comparable<Mission>{
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mNeedFood;
    private String mAward;
    private double mDistance;
    private boolean mSolved;
    private boolean mAccepted;
    private LatLng location;
    private Marker marker;

    public Mission(){
        //生成唯一ID
        mId = UUID.randomUUID();
        mDate = new Date();
        mSolved = false;
        mAccepted = false;
    }

    @Override
    public int compareTo(Mission m){
        return (int)(this.getDistanceValue()-m.getDistanceValue());
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
        if (mDistance>1000)
            return String.format("%.2f",mDistance/1000)+" 千米";
        else
            return Math.round(mDistance) + " 米";
    }

    public double getDistanceValue(){
        return mDistance;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public boolean isAccepted() {
        return mAccepted;
    }

    public void setAccepted(boolean mAccepted) {
        this.mAccepted = mAccepted;
    }
}
