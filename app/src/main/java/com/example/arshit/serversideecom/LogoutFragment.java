package com.example.arshit.serversideecom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.UUID;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
    }

    TextView textView;
    Integer id;
    RelativeLayout relativeLayout;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.logout, container, false);
        init(view);
        return view;


    }

    private void init(View view) {

        relativeLayout  = view.findViewById(R.id.container_single_item);
        textView = view.findViewById(R.id.logout);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

                SharedPreferences.Editor editor = settings.edit();
                editor.remove("username");


                Intent intent = new Intent(getContext(),SignIn.class);
                startActivity(intent);
                getActivity().finish();


            }
        });
    }
}
