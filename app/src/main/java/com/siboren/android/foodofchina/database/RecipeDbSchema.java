package com.siboren.android.foodofchina.database;

public class RecipeDbSchema {
    public static final class RecipeTable{
        public static final String NAME="recipes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String NEEDMATERIAL = "needmaterial";
            public static final String NUM = "num";
        }
    }
}