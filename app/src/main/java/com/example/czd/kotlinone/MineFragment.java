package com.example.czd.kotlinone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.czd.util.SQLiteDbHelper;

public class MineFragment extends Fragment {
    String name ;
    int shenfen;
    Button btn;
    Button btncanle;
    Button btnreg;
    Button quesme;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        btn = view.findViewById(R.id.login);
        btncanle = view.findViewById(R.id.Cancellation);
        btnreg = view.findViewById(R.id.register);
        quesme = view.findViewById(R.id.quesme);
        Button btnrecord = view.findViewById(R.id.record);
        btncanle.setVisibility(View.INVISIBLE);
        quesme.setVisibility(View.INVISIBLE);
        //读取sharedpreferencce
        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        name = preferences.getString("username","moren");
        shenfen = preferences.getInt("shenfen",0);

        SQLiteDbHelper dbHelper = new SQLiteDbHelper(getActivity());
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (!name.equals("moren")){
            //Toast.makeText(getActivity(),name,Toast.LENGTH_SHORT).show();
            btn.setVisibility(View.INVISIBLE);
            btnreg.setVisibility(View.INVISIBLE);
            btncanle.setVisibility(View.VISIBLE);
        }else{
            //Toast.makeText(getActivity(),name,Toast.LENGTH_SHORT).show();
            btn.setVisibility(View.VISIBLE);
            btnreg.setVisibility(View.VISIBLE);
            btncanle.setVisibility(View.INVISIBLE);
        }
        if (shenfen==1){
            quesme.setVisibility(View.VISIBLE);
        }else {
            quesme.setVisibility(View.INVISIBLE);
        }

        quesme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), QuesmeActivity.class);
                getActivity().startActivity(intent);
            }
        });
        btnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RecordActivity.class);
                getActivity().startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RegisterActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btncanle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sharedpreference
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                editor.putString("username","moren");
                editor.putInt("shenfen",0);
                editor.apply();
                Intent intent=new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //读取sharedpreferencce
        SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        name = preferences.getString("username","moren");
        shenfen = preferences.getInt("shenfen",0);

        if (!name.equals("moren")){
            //Toast.makeText(getActivity(),name,Toast.LENGTH_SHORT).show();
            btn.setVisibility(View.INVISIBLE);
            btnreg.setVisibility(View.INVISIBLE);
            btncanle.setVisibility(View.VISIBLE);
        }else{
            //Toast.makeText(getActivity(),name,Toast.LENGTH_SHORT).show();
            btn.setVisibility(View.VISIBLE);
            btnreg.setVisibility(View.VISIBLE);
            btncanle.setVisibility(View.INVISIBLE);
        }
        if (shenfen==1){
            quesme.setVisibility(View.VISIBLE);
        }else {
            quesme.setVisibility(View.INVISIBLE);
        }
    }
}
