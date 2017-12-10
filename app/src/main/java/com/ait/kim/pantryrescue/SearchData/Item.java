package com.ait.kim.pantryrescue.SearchData;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kimpham on 12/10/17.
 */

public class Item extends RealmObject {
    private String itemName;

    @PrimaryKey
    private String itemID;

    public Item() {

    }

    public Item(String itemName) {
        this.itemName = itemName;

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


}