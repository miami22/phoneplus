package com.parhamcodeappsgmail.phoneplus.Fragment.Log;

public class itemlog {

    int id;
    String number;
    String date;
    String dayofweek;
    String duration;
    String type;
    String count;
    String simnum;
    String seperator;
    String shamsi;
    String time;



    public itemlog(int id,String number, String date, String dayofweek, String duration, String type,String count,String simnum,String seperator,String shamsi,String time){
        this.number=number;
        this.date =date;
        this.dayofweek =dayofweek;
        this.duration=duration;
        this.type=type;
        this.id=id;
        this.count=count;
        this.simnum=simnum;
        this.seperator=seperator;
        this.shamsi=shamsi;
        this.time=time;

    }
    public String getNumber(){
        return number;
    }
    public String getDayofweek(){ return dayofweek; }
    public String getDuration(){
        return  duration;
    }
    public String getType(){
        return  type;
    }
    public String getdate(){
        return  date;
    }
    public int getId(){
        return  id;
    }
    public String getCount(){
        return  count;
    }
    public String getSimnum(){ return  simnum; }

    public String getSeperator() {
        return seperator;
    }

    public String getShamsi() {
        return shamsi;
    }

    public String getTime() {
        return time;
    }

    public void setSimnum(String simnum) {
        this.simnum = simnum;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) { this.time = time; }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }
}
