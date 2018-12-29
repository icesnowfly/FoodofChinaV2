package com.siboren.android.foodofchina;

public class Bag {
    public static final String TABLE="bag";

    public String uid;
    public String item_name;
    public int num;//默认为0

    public static final String KEY_uid="uid";
    public static final String KEY_item_name="item_name";
    public static final String KEY_num="num";

    public Bag(String uid){
        this.uid=uid;
        this.item_name="";
        this.num=0;
    }
}
