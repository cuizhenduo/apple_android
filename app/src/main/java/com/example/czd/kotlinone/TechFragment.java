package com.example.czd.kotlinone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TechFragment extends Fragment {
    View view;
    public String[] data = {"主要品种","建园","土壤肥水管理","整型修剪","花果采收","病虫害","机械操作","其他"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tech_fragment, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);
        ListView listView = (ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getActivity(),Main2Activity.class);
                //startActivity(intent);

                Intent intent = new Intent();
                intent.putExtra("mark", data[position]);
                intent.setClass(getActivity(), Main2Activity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }



}
