package com.example.czd.bean;

public class TitleList {
    public int pid;
    public String title;
    public TitleList(int pid,String title){
        this.pid=pid;
        this.title=title;
    }
    public int getId(){
        return pid;
    }

    public String getTitle() {
        return title;
    }
}
