package com.example.czd.kotlinone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.czd.bean.TitleList;
import com.example.czd.kotlinone.adapter.TitleListAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {
    TitleList titleList;
    List<TitleList> titleLists  = new ArrayList();
    int page = 1;
    ListView listView;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        listView = (ListView)view.findViewById(R.id.list_view);
        btn = (Button)view.findViewById(R.id.ques);
        sendrequesturl("http://39.104.27.39:8995/luntandata?page=1","re");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("pid", titleLists.get(position).getId()+"");
                        intent.setClass(getActivity(), ShowTieActivity.class);
                        getActivity().startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //读取sharedpreferencce
            SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            String name = preferences.getString("username","moren");
            int shenfen = preferences.getInt("shenfen",0);
            if (name.equals("moren")){
                Toast.makeText(getActivity(),"您还未登录",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent();
                intent.setClass(getActivity(), FatieActivity.class);
                getActivity().startActivity(intent);
            }
            }
        });
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                sendrequesturl("http://39.104.27.39:8995/luntandata?page=1","re");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("pid", titleLists.get(position).getId()+"");
                        intent.setClass(getActivity(), ShowTieActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                listView.setStackFromBottom(false);
                refreshlayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
                page = 2;
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                sendrequesturl("http://39.104.27.39:8995/luntandata?page="+page,"mo");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("pid", titleLists.get(position).getId()+"");
                        intent.setClass(getActivity(), ShowTieActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                listView.setStackFromBottom(true);
                refreshlayout.finishLoadmore();
                page += 1;
            }
        });


        return view;

    }
    public void sendrequesturl(final String ourl, final String kind){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL(ourl);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    if(response.toString()==""){
                        page = page - 1;
                    }else{
                        showResponse(response.toString(),kind);
                    }
                    //showResponse(response.toString());

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try {
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (connection !=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public void showResponse(final String response,final String kind){
        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run() {
                parseJson(response,kind);
            }
        });
    }

    public void parseJson(String jsdata,String kind){
        try {
            if (kind == "re"){
                titleLists.clear();
            }
            JSONObject jsonObject = new JSONObject(jsdata);
            JSONArray jsonArray = jsonObject.getJSONArray("tit");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int id = jsonObject1.getInt("id");
                String tit = jsonObject1.getString("title");
                titleList = new TitleList(id,tit);
                titleLists.add(titleList);
            }
            TitleListAdapter titleListAdapter = new TitleListAdapter(getActivity(),R.layout.title_item,titleLists);
            listView.setAdapter(titleListAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}