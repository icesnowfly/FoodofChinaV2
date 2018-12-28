package com.siboren.android.foodofchina;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private static final String EXTRA_USER_ID=
            "com.siboren.android.foodname.user_id";

    private Button mLoginButton;
    private Button mRegisterButton;
    private Context mContext;

    public UserAPI userapi;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        // Log.d("main_activity", "onCreate: check finished " +j);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        userapi = new UserAPI(mContext);
        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mRegisterButton = (Button) v.findViewById(R.id.register_button);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jj = ((EditText) getActivity().findViewById(R.id.login_user)).getText().toString();
                if (jj.length() == 0) {
                    return;
                }
                int j = userapi.login(Integer.parseInt(jj),
                        ((EditText) getActivity().findViewById(R.id.login_password)).getText().toString());
                if (j == 1) {
                    Intent i = MapActivity.newIntent(mContext,jj);
                    startActivity(i);
                    getActivity().onBackPressed();
                } else {
                    if (j == 0) {
                        Toast.makeText(mContext, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, R.string.null_username, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(mContext, RegActivity.class);
                startActivity(k);
            }
        });
    }
}
