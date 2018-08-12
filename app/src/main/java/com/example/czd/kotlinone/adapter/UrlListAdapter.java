package com.example.czd.kotlinone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.czd.bean.TitleList;
import com.example.czd.bean.UrlList;
import com.example.czd.kotlinone.R;

import java.util.List;

public class UrlListAdapter extends ArrayAdapter<UrlList> {
    private int resourceID;
    public UrlListAdapter(Context context, int itemresourceId, List<UrlList> objects){
        super(context,itemresourceId,objects);
        resourceID = itemresourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UrlList urlList = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        TextView title = (TextView)view.findViewById(R.id.title_context);
        title.setText(urlList.getTitle());
        return view;
    }
}
