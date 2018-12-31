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
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
        }
    }

    public static final class AwardTable{
        public static final String NAME="award";

        public static final class Cols {
            public static final String MISSIONID = "missionId";
            public static final String TITLE = "title";
            public static final String NUM = "num";
        }
    }

    public static final class MaterialTable{
        public static final String NAME="material";

        public static final class Cols {
            public static final String RECIPEID = "recipeId";
            public static final String TITLE = "title";
            public static final String NUM = "num";
        }
    }

    public static final class RecipeTable{
        public static final String NAME="recipes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String NEEDMATERIAL = "needMaterial";
            public static final String NUM = "num";
        }
    }

    public static final class BagTable{
        public static final String NAME="bag";

        public static final class Cols {
            public static final String TITLE = "title";
            public static final String NUM = "num";
        }
    }

    public static final class UserTable{
        public static final String NAME="user";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String ACCOUNT = "account";
            public static final String PASSWORD = "password";
            public static final String LEVEL = "level";
            public static final String EXP = "exp";
            public static final String MONEY = "money";
        }
    }
}
