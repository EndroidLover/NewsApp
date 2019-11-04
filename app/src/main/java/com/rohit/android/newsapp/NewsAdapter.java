package com.rohit.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> list){
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;
        if(listview == null){
            listview = LayoutInflater.from(getContext()).inflate(R.layout.listview,parent,false);
        }

        final News currentNews = getItem(position);
        TextView title = (TextView)listview.findViewById(R.id.title);
        title.setText(currentNews.getTitle());

        TextView date = listview.findViewById(R.id.date);
        date.setText(currentNews.getUrl());

        return listview;
    }
}
