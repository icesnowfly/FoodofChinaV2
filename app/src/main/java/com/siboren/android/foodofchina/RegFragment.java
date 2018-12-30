package com.siboren.android.foodofchina;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {
                   //String username=findViewById(R.id.reg_user).toString();
                   User user=new User();
                   user.name=((EditText)getActivity().findViewById(R.id.reg_user)).getText().toString();
                   user.password=((EditText)getActivity().findViewById(R.id.reg_password)).getText().toString();
                   String check_password=((EditText)getActivity().findViewById(R.id.reg_check_password)).getText().toString();
                   if (user.password.equals(check_password))
                   {
                       UserData.insert(user);
                       Toast.makeText(mContext,R.string.reg_success,Toast.LENGTH_SHORT).show();
                       getActivity().onBackPressed();
                   }else{
                       Toast.makeText(mContext,R.string.password_dif,Toast.LENGTH_SHORT).show();
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
}
