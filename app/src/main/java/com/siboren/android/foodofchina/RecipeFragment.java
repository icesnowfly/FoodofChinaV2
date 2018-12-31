package com.siboren.android.foodofchina;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.UUID;

public class RecipeFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";

    private Recipe mRecipe;
    private TextView mTitleField;
    private TextView mRecipeMaterial;
    private Button mCompoundButton;
    private TextView mRecipeNum;
    private RecipeLab mRecipeLab = RecipeLab.get(getActivity());

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
        mRecipe = mRecipeLab.getRecipe(recipeId);
    }

    @Override
    public void onPause(){
        super.onPause();
        mRecipeLab.updateRecipe(mRecipe);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_recipe,container,false);

        mTitleField = (TextView)v.findViewById(R.id.recipe_title);
        mTitleField.setText(mRecipe.getTitle());
        mRecipeMaterial = (TextView)v.findViewById(R.id.recipe_material);
        mRecipeMaterial.setText(mRecipeLab.getRecipeMaterial(mRecipe.getId()));
        mRecipeNum=(TextView)v.findViewById(R.id.recipe_num);
        mRecipeNum.setText(String.valueOf(mRecipe.getNum()));
        mCompoundButton=(Button)v.findViewById(R.id.recipe_compound);
        mCompoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecipeLab.checkRecipe(mRecipe)) {
                    mRecipe.setNum(mRecipe.getNum() + 1);
                    mRecipeLab.compoundRecipe(mRecipe);
                    mRecipeNum.setText(String.valueOf(mRecipe.getNum()));
                    mRecipeMaterial.setText(mRecipeLab.getRecipeMaterial(mRecipe.getId()));
                }
                else Toast.makeText(getActivity(),R.string.NotEnoughMaterial,Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
