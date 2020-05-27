package com.k.wechat.robot.action;

public class SendNewsReady {
    public static boolean setNews = false;
    public SendNewsReady() {
    }

    public static boolean ReadyNews(){
        setNews = true;
        return  setNews;
    }

    public static boolean nReadyNews() {
        setNews = false;
        return setNews;
    }

    public static boolean isReadyNews() {
        return setNews;
    }
}
