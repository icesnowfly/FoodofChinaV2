package com.siboren.android.foodofchina;

import android.support.v4.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {

    private String user_id;

    @Override
    protected Fragment createFragment(){
        return new RecipeListFragment();
    }

}
