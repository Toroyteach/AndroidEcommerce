package com.bellenorthe.fruity.models;

import java.io.Serializable;

public class GenericProductModel implements Serializable {

    public int cardid;
    public String cardname;
    public String cardimage;
    public String carddiscription;
    public String cardprice;
    public String status;

    public GenericProductModel() {
    }

    public GenericProductModel(int cardid, String cardname, String cardimage, String carddiscription, String cardprice, String status) {
        this.cardid = cardid;
        this.cardname = cardname;
        this.cardimage = cardimage;
        this.carddiscription = carddiscription;
        this.cardprice = cardprice;
        this.status = status;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardimage() {
        return cardimage;
    }

    public void setCardimage(String cardimage) {
        this.cardimage = cardimage;
    }

    public String getCarddiscription() {
        return carddiscription;
    }

    public void setCarddiscription(String carddiscription) {
        this.carddiscription = carddiscription;
    }

    public String getCardprice() {
        return cardprice;
    }

    public void setCardprice(String cardprice) {
        this.cardprice = cardprice;
    }

    public int getCardid() {
        return cardid;
    }

    public void setCardid(int cardid) {
        this.cardid = cardid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}