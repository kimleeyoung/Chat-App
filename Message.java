package kr.ac.mjc.finalproject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String id;
    private String text;
    private String writerId;
    private Date date;

    public Message(){
        date=new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormattedTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm"); //13:00
        return sdf.format(this.date);
    }
}