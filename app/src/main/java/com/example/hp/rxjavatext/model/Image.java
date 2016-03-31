package com.example.hp.rxjavatext.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hp on 2016/3/31.
 */
public class Image extends RealmObject{


    @PrimaryKey
    private String id;

    private String who;
    private String publishedAt;
    private String desc;
    private String type;
    private String url;
    private boolean used;

    private String createdAt;


    public static Image queryImageById(Realm realm,String id){

        RealmResults<Image> realmResults=realm.where(Image.class).equalTo("id",id).findAll();
        if(realmResults.size()>0){

            Image image=realmResults.get(0);
            return image;
        }
        return null;
    }

    public static Image GoodCopyToImage(Goods goods,Image image){
        image.setWho(goods.getWho());
        image.setPublishedAt(goods.getPublishedAt());
        image.setDesc(goods.getDesc());
        image.setType(goods.getType());
        image.setUrl(goods.getUrl());
        image.setUsed(goods.isUsed());
        image.setId(goods.getObjectId());
        image.setCreatedAt(goods.getCreatedAt());

        return image;
    }


    public String getId() {
        return id;
    }

    public String getWho() {
        return who;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public boolean isUsed() {
        return used;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
