package com.siboren.android.foodofchina;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MissionLab {
    private static MissionLab sMissionLab;
    private List<Mission> mMissions;

    public static MissionLab get(Context context){
        if (sMissionLab == null){
            sMissionLab = new MissionLab(context);
        }
        return sMissionLab;
    }

    private MissionLab(Context context){
        mMissions = new ArrayList<>();
        Mission mission = new Mission();
        mission.setTitle("Mission #1");
        mission.setDistance(2.4);
        mission.setNeedFood("food #1");
        mission.setAward("money x 3000"+"\nexp x 5000");
        mission.setSolved(false);
        mMissions.add(mission);
    }

    public List<Mission> getMissions(){
        return mMissions;
    }

    public Mission getMission(UUID id){
        for (Mission mission:mMissions){
            if (mission.getId().equals(id)){
                return mission;
            }
        }
        return null;
    }

    public void addMission(Mission mission)
    {
        mMissions.add(mission);
    }
}
