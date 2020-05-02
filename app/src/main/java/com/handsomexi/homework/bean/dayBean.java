package com.handsomexi.homework.bean;

public class dayBean {
    public int id;
    public String uid;
    public String img_path;
    public String time;
    public String reviews;
    public String status;    //对错情况
    public String mistakes;  //错误次数

    public dayBean( String uid, String img_path, String time, String reviews, String status, String mistakes) {
        this.uid = uid;
        this.img_path = img_path;
        this.time = time;
        this.reviews = reviews;
        this.status = status;
        this.mistakes = mistakes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMistakes() {
        return mistakes;
    }

    public void setMistakes(String mistakes) {
        this.mistakes = mistakes;
    }
}
