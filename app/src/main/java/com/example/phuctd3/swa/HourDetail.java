package com.example.phuctd3.swa;

/**
 * Created by PhucTD3 on 8/23/2017.
 */

public class HourDetail {

    private String temp;
    private String des;
    private String time;
    private String icon;

    public HourDetail(){
        temp = "0";
        des = "";
    }

    public HourDetail(String temp, String des, String time, String icon) {
        this.temp = temp;
        this.des = des;
        this.time = time;
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
