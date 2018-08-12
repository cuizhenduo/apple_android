package com.example.czd.bean;

public class UrlList {
    public String url;
    public String title;
    public UrlList(String title,String url){
        this.url=url;
        this.title=title;
    }
    public String getUrl(){
        return url;
    }

    public String getTitle() {
        return title;
    }
}
