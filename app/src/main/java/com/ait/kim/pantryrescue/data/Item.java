package com.ait.kim.pantryrescue.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimpham on 12/10/17.
 */

public class Item {
    private String uid;
    private String itemName;

    @PrimaryKey
    private String itemID;

    public Item() {

    }

    public Item(String uid, String itemName) {
        this.uid = uid;
        this.itemName = itemName;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


}