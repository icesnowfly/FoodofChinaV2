package com.siboren.android.foodofchina;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RecipeListFragment extends Fragment {

    private int current_changed_item;
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_recipe_list,container,false);

        mRecipeRecyclerView = (RecyclerView) view
                .findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        RecipeLab recipeLab = RecipeLab.get(getActivity());
        List<Recipe> recipes = recipeLab.getRecipes();

        if (mAdapter==null) {
            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setRecipes(recipes);
            if (current_changed_item!=-1)
                mAdapter.notifyItemChanged(current_changed_item);
            else mAdapter.notifyDataSetChanged();
        }
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Recipe mRecipe;
        private int position;
        private TextView mTitleTextView;
        private TextView mNumTextView;
        private Button mCompoundButton;

        public RecipeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_recipe_title_text_view);
            mNumTextView=(TextView)
                    itemView.findViewById(R.id.list_item_recipe_num_text_view);
            mCompoundButton=(Button)
                    itemView.findViewById(R.id.list_item_recipe_compound_button);
        }

        public void bindRecipe(Recipe recipe){
            mRecipe = recipe;
            mTitleTextView.setText(mRecipe.getTitle());
            mNumTextView.setText(String.valueOf(mRecipe.getNum()));
        }

        public void setPosition(int pos){
            position=pos;
        }

        @Override
        public void onClick(View v){
            Intent intent=RecipePagerActivity.newIntent(getActivity(),mRecipe.getId());
            current_changed_item=-1;
            startActivity(intent);
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder>{
        private List<Recipe> mRecipes;
        public RecipeAdapter(List<Recipe> recipes){
            mRecipes = recipes;
        }

        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_recipe,parent,false);
            return new RecipeHolder(view);
        }

        public void setRecipes(List<Recipe> recipes){
            mRecipes = recipes;
        }

        @Override
        public void onBindViewHolder(RecipeHolder holder,int position){
            Recipe recipe=mRecipes.get(position);
            holder.setPosition(position);
            holder.bindRecipe(recipe);
        }

        @Override
        public int getItemCount(){
            return mRecipes.size();
        }
    }
}
