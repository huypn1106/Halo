package com.deadk.halo.models;

import java.io.Serializable;

public class FriendRequest implements Serializable {

    private String uid;
    private String date;

    public FriendRequest() {
    }

    public FriendRequest(String uid, String date) {
        this.uid = uid;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
