package com.siboren.android.foodofchina;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegFragment extends Fragment {

    private Button mRegisterButton;
    private Button mBackButton;
    private UserAPI UserData;
    private Context mContext;
    private RecipeLab mRecipeLab;
    private UserLab mUserLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.fragment_reg ,container,false);
        UserData = new UserAPI(mContext);
        mRegisterButton=(Button)v.findViewById(R.id.register_in_button);
        mBackButton=(Button)v.findViewById(R.id.back_button);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mRecipeLab=RecipeLab.get(mContext);
        mUserLab=UserLab.get(mContext);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {
                   //String username=findViewById(R.id.reg_user).toString();
                   User user = new User();
                   user.setAccount(((EditText)getActivity().findViewById(R.id.reg_user)).getText().toString());
                   user.setPassword(((EditText)getActivity().findViewById(R.id.reg_password)).getText().toString());
                   String check_password=((EditText)getActivity().findViewById(R.id.reg_check_password)).getText().toString();
                   if (user.getAccount().equals("") || user.getPassword().equals("") || check_password.equals(""))
                       Toast.makeText(mContext, R.string.NotCompleteTable, Toast.LENGTH_SHORT).show();
                   else {
                       if (user.getPassword().equals(check_password)) {
                           if (mUserLab.isUserExist(user.getAccount())) {
                               mUserLab.InsertUser(user);
                               InitialUserDatabase();
                               Toast.makeText(mContext, R.string.reg_success, Toast.LENGTH_SHORT).show();
                               getActivity().onBackPressed();
                           } else
                               Toast.makeText(mContext, R.string.same_username, Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(mContext, R.string.password_dif, Toast.LENGTH_SHORT).show();
                       }
                   }
               }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void InitialUserDatabase(){
        Recipe recipe = new Recipe();
        recipe.setTitle("番茄炒蛋");
        recipe.setNum(0);
        mRecipeLab.addRecipe(recipe);
        mRecipeLab.addMaterial(recipe.getId(),"番茄",10);
        mRecipeLab.addMaterial(recipe.getId(),"鸡蛋",10);
        recipe = new Recipe();
        recipe.setTitle("麻婆豆腐");
        recipe.setNum(0);
        mRecipeLab.addRecipe(recipe);
        mRecipeLab.addMaterial(recipe.getId(),"豆腐",15);
        mRecipeLab.addMaterial(recipe.getId(),"辣椒",10);
        recipe = new Recipe();
        recipe.setTitle("糖醋排骨");
        recipe.setNum(0);
        mRecipeLab.addRecipe(recipe);
        mRecipeLab.addMaterial(recipe.getId(),"排骨",10);
        mRecipeLab.addMaterial(recipe.getId(),"调料",5);
        mRecipeLab.insertBag("番茄",100);
        mRecipeLab.insertBag("鸡蛋",100);
        mRecipeLab.insertBag("豆腐",100);
        mRecipeLab.insertBag("辣椒",100);
        mRecipeLab.insertBag("排骨",100);
        mRecipeLab.insertBag("调料",100);
    }
}
