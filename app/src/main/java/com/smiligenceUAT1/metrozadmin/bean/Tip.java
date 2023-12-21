package com.smiligenceUAT1.metrozadmin.bean;

public class Tip
{
    String creationDate;
    String url;
    int tipsAmount;
    String tipsName;
    String tipsSelectedStatus;

    public String getTipsSelectedStatus() {
        return tipsSelectedStatus;
    }

    public void setTipsSelectedStatus(String tipsSelectedStatus) {
        this.tipsSelectedStatus = tipsSelectedStatus;
    }

    public int getTipsAmount() {
        return tipsAmount;
    }

    public void setTipsAmount(int tipsAmount) {
        this.tipsAmount = tipsAmount;
    }

    public String getTipsName() {
        return tipsName;
    }

    public void setTipsName(String tipsName) {
        this.tipsName = tipsName;
    }

    public Tip() {
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
