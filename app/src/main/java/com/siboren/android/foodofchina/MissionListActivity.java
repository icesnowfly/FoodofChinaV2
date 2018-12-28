package com.siboren.android.foodofchina;

import android.support.v4.app.Fragment;

public class MissionListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new MissionListFragment();
    }
}
