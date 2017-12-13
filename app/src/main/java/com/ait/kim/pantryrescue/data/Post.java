package com.ait.kim.pantryrescue.data;

/**
 * Created by alicetan on 12/11/17.
 */

public class Post {

//    private String userId;
//    private String author;
//    private String title;
//    private String body;
//    private String imgUrl;
//
//    public Post(){
//
//    }
//
//    public Post(String userId, String author, String title, String body) {
//        this.userId = userId;
//        this.author = author;
//        this.title = title;
//        this.body = body;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public String getImgUrl() {
//        return imgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        this.imgUrl = imgUrl;
//    }

    private String uid;
    private String author;
    private String title;
    private String body;

    public Post(){

    }

    public Post (String uid, String author, String title, String body){
        this.author = author;
        this.uid = uid;
        this.title = title;
        this.body = body;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}