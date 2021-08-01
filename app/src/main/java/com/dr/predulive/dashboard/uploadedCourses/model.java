package com.dr.predulive.dashboard.uploadedCourses;

public class model {
    public model() {
    }

    String made_by, desc, title, purl,video_url,pdf_url;

    public model(String made_by, String desc, String title, String purl, String video_url, String pdf_url) {
        this.made_by = made_by;
        this.desc = desc;
        this.title = title;
        this.purl = purl;
        this.video_url = video_url;
        this.pdf_url = pdf_url;
    }

    public String getMade_by() {
        return made_by;
    }

    public void setMade_by(String made_by) {
        this.made_by = made_by;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }
}

