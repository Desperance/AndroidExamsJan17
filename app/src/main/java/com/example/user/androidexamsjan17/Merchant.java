package com.example.user.androidexamsjan17;

/**
 * Created by user on 27/1/2017.
 */

public class Merchant {

    private String id;
    private String legalName;
    private String category;
    private String address;
    private String imageUrl;
    private String review;




    public String getLegalName(){
        return legalName;
    }

    public void setLegalName(String a){
         legalName = a;
    }
    public void setCategory(String a){
        category = a;
    }
    public void setAddress(String a){
        address = a;
    }
    public void setReview(String a){
        review = a;
    }
}