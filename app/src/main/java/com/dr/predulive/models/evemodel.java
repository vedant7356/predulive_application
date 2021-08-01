package com.dr.predulive.models;

public class evemodel {
    public evemodel() {
    }

    String Details_Of_Events,Joining_url,Title,date;

    public evemodel(String details_Of_Events, String joining_url, String title, String date) {
        Details_Of_Events = details_Of_Events;
        Joining_url = joining_url;
        Title = title;
        this.date = date;
    }

    public String getDetails_Of_Events() {
        return Details_Of_Events;
    }

    public void setDetails_Of_Events(String details_Of_Events) {
        Details_Of_Events = details_Of_Events;
    }

    public String getJoining_url() {
        return Joining_url;
    }

    public void setJoining_url(String joining_url) {
        Joining_url = joining_url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
