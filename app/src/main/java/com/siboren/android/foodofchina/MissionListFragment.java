package com.siboren.android.foodofchina;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MissionListFragment extends Fragment {

    private int current_changed_item;
    private RecyclerView mMissionRecyclerView;
    private MissionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_mission_list,container,false);

        mMissionRecyclerView = (RecyclerView) view
                .findViewById(R.id.mission_recycler_view);
        mMissionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ((SimpleItemAnimator)mMissionRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        MissionLab missionLab = MissionLab.get(getActivity());
        List<Mission> missions = missionLab.getMissions();

        if (mAdapter==null) {
            mAdapter = new MissionListFragment.MissionAdapter(missions);
            mMissionRecyclerView.setAdapter(mAdapter);
        }else{
            if (current_changed_item!=-1)
                mAdapter.notifyItemChanged(current_changed_item);
            else mAdapter.notifyDataSetChanged();
        }
    }

    private class MissionHolder extends RecyclerView.ViewHolder{
        private Mission mMission;
        private int position;
        private TextView mTitleTextView;
        private TextView mNeedFoodTextView;
        private TextView mAwardTextView;
        private TextView mDistanceTextView;
        private Button mAcceptButton;

        public MissionHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_mission_title_text_view);
            mNeedFoodTextView=(TextView)
                    itemView.findViewById(R.id.list_item_mission_need_food_text_view);
            mAwardTextView=(TextView)
                    itemView.findViewById(R.id.list_item_mission_award_text_view);
            mAcceptButton=(Button)
                    itemView.findViewById(R.id.list_item_mission_accept_button);
            mDistanceTextView=(TextView)
                    itemView.findViewById(R.id.list_item_mission_distance_text_view);
        }

        public void bindMission(Mission mission){
            mMission = mission;
            mTitleTextView.setText(mMission.getTitle());
            mNeedFoodTextView.setText(mMission.getNeedFood());
            mAwardTextView.setText(mMission.getAward());
            mDistanceTextView.setText(mMission.getDistance());
            if (!mMission.isSolved()) {
                mAcceptButton.setText(getString(R.string.accept));
                mAcceptButton.setActivated(false);
                mAcceptButton.setBackground(getResources().getDrawable(R.drawable.btn_circle_colored));
            }
            else {
                mAcceptButton.setText(getString(R.string.cancel));
                mAcceptButton.setBackground(getResources().getDrawable(R.drawable.btn_circle));
            }
            mAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mMission.isSolved()) {
                        mMission.setSolved(true);
                        Toast.makeText(getActivity(),R.string.mission_accept,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.hint);
                        builder.setMessage(R.string.ask_mission_cancel_message);
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMission.setSolved(false);
                                Toast.makeText(getActivity(),R.string.mission_cancel,Toast.LENGTH_SHORT).show();
                                updateUI();
                            }
                        });
                        builder.setNegativeButton(R.string.no,null);
                        builder.create().show();
                    }
                    current_changed_item=position;
                    updateUI();
                }
            });
        }

        public void setPosition(int pos){
            position=pos;
        }
    }

    private class MissionAdapter extends RecyclerView.Adapter<MissionListFragment.MissionHolder>{
        private List<Mission> mMissions;
        public MissionAdapter(List<Mission> missions){
            mMissions = missions;
        }

        @Override
        public MissionListFragment.MissionHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_mission,parent,false);
            return new MissionListFragment.MissionHolder(view);
        }

        @Override
        public void onBindViewHolder(MissionListFragment.MissionHolder holder, int position){
            Mission mission=mMissions.get(position);
            holder.setPosition(position);
            holder.bindMission(mission);
        }

        @Override
        public int getItemCount(){
            return mMissions.size();
        }
    }
}
