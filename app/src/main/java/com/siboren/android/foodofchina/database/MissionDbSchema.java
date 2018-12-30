package com.siboren.android.foodofchina.database;

public class MissionDbSchema {
    public static final class MissionTable{
        public static final String NAME="missions";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String AWARD = "award";
            public static final String NEEDFOOD = "needfood";
            public static final String SOLVED = "solved";
            public static final String ACCEPTED = "accepted";
            public static final String DISTANCE = "distance";
            public static final String LOCATION = "location";
        }
    }
}
