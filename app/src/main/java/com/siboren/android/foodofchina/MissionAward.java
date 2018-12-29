package com.siboren.android.foodofchina;

public class MissionAward {
    public static final String TABLE="missionAward";

    public String title;
    public String item_name;
    public int num;//默认为0

    public static final String KEY_title="title";
    public static final String KEY_item_name="item_name";
    public static final String KEY_num="num";

    public MissionAward(String title){
        this.title=title;
        this.item_name="";
        this.num=0;
    }
}
