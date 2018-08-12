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
import com.example.czd.kotlinone.R;

import java.util.List;

public class TitleListAdapter extends ArrayAdapter<TitleList> {
    private int resourceID;
    public TitleListAdapter(Context context, int itemresourceId, List<TitleList> objects){
        super(context,itemresourceId,objects);
        resourceID = itemresourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TitleList titleList = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        TextView title = (TextView)view.findViewById(R.id.title_context);
        title.setText(titleList.getTitle());
        return view;
    }
}
