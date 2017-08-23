package com.example.phuctd3.swa;

/**
 * Created by PhucTD3 on 8/23/2017.
 */

public class HourDetail {

    private String temp;
    private String des;
    private String time;

    public HourDetail(){
        temp = "0";
        des = "";
    }

    public HourDetail(String temp, String des, String time) {
        this.temp = temp;
        this.des = des;
        this.time = time;
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
}
