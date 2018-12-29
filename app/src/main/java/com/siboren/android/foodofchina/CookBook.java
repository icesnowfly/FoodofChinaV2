package com.siboren.android.foodofchina;

public class CookBook {
    public static final String TABLE="cookbook";

    public String title;//菜名
    public String material;//材料名
    public int num;//需要数量

    public static final String KEY_title="title";
    public static final String KEY_material="material";
    public static final String KEY_num="num";

    public CookBook(String title,String material,int num){
        this.title=title;
        this.material=material;
        this.num=num;
    }
    public CookBook(String title){
        this.title=title;
        this.material="";
        this.num=0;
    }
}
