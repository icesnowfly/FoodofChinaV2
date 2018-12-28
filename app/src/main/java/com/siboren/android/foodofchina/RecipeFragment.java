package com.siboren.android.foodofchina;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.UUID;

public class RecipeFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";

    private Recipe mRecipe;
    private TextView mTitleField;
    private TextView mRecipeMaterial;
    private Button mCompoundButton;
    private TextView mRecipeNum;

    public static RecipeFragment newInstance(UUID recipeID){
        Bundle args=new Bundle();
        args.putSerializable(ARG_RECIPE_ID,recipeID);

        RecipeFragment fragment=new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID recipeId = (UUID) getArguments().getSerializable(ARG_RECIPE_ID);
        mRecipe = RecipeLab.get(getActivity()).getRecipe(recipeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_recipe,container,false);

        mTitleField = (TextView)v.findViewById(R.id.recipe_title);
        mTitleField.setText(mRecipe.getTitle());
        mRecipeNum=(TextView)v.findViewById(R.id.recipe_num);
        mRecipeNum.setText(String.valueOf(mRecipe.getNum()));
        mCompoundButton=(Button)v.findViewById(R.id.recipe_compound);
        mCompoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecipe.setNum(mRecipe.getNum()+1);
                mRecipeNum.setText(String.valueOf(mRecipe.getNum()));
            }
        });
        return v;
    }
}
