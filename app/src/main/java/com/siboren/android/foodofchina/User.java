package com.siboren.android.foodofchina;

import java.util.UUID;

public class User {
    private UUID mId;
    private String mAccount;
    private String mPassword;
    private int mLevel;
    private int mExp;
    private int mMoney;

    public User(){
        this(UUID.randomUUID());
    }

    public User(UUID id){
        mId = id;
        mLevel=1;
        mExp=0;
        mMoney=0;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String mAccount) {
        this.mAccount = mAccount;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public UUID getId() {
        return mId;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public int getExp() {
        return mExp;
    }

    public void setExp(int mExp) {
        this.mExp = mExp;
    }

    public int getMoney() {
        return mMoney;
    }

    public void setMoney(int mMoney) {
        this.mMoney = mMoney;
    }
}
