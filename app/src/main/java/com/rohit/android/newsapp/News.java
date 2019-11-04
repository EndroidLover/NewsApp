package com.rohit.android.newsapp;

public class News {

    String title;
    String url;

    public News(String a,String b){
        title = a;
        url = b;
    }

    public String getTitle(){
        return title;

    }
    public String getUrl(){
        return url;
    }
}
