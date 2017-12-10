package com.ait.kim.pantryrescue;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by kimpham on 12/10/17.
 */

public class ItemApplication extends Application {

    private Realm realmItem;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmItem = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmItem.close();
    }

    public Realm getRealmItem() {
        return realmItem;
    }
}