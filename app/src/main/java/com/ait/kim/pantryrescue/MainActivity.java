package com.ait.kim.pantryrescue;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ait.kim.pantryrescue.adapter.ItemRecyclerAdapter;
import com.ait.kim.pantryrescue.data.Item;
import com.ait.kim.pantryrescue.touch.ItemTouchHelperCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {


    public static final String ITEM_NAME = "ITEM_NAME";
    private ItemRecyclerAdapter adapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ItemApplication) getApplication()).openRealm();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.action_add:
                                showAddDialog();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_about:
                                Toast.makeText(MainActivity.this, "Made by Alice, Kim, and Sarah", Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_logout:
                                FirebaseAuth.getInstance().signOut();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                finish();
                                break;

                        }

                        return false;
                    }
                });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icons8_menu_48);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();


            }
        });

        RealmResults<Item> allItems = getRealm().where(Item.class).findAll();
        Item itemsArray[] = new Item[allItems.size()];
        List<Item> itemsResult = new ArrayList<Item>(Arrays.asList(allItems.toArray(itemsArray)));

        adapter = new ItemRecyclerAdapter(this, ((ItemApplication) getApplication()).getRealmItem());


        RecyclerView recyclerViewItem = (RecyclerView) findViewById(R.id.recyclerItem);

        recyclerViewItem.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItem.setAdapter(adapter);


        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewItem);



    }

    private void showAddDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Separate ingredients by comma or enter keyword");
        final EditText input = new EditText(MainActivity.this);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(MainActivity.this, "Input required", Toast.LENGTH_SHORT).show();
                } else
                    adapter.addItem(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public Realm getRealm() {

        return ((ItemApplication) getApplication()).getRealmItem();
    }

    public void deleteItem(Item item) {
        getRealm().beginTransaction();
        item.deleteFromRealm();
        getRealm().commitTransaction();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ItemApplication) getApplication()).closeRealm();
    }


    public void showRecipeActivity(String item) {
        Intent newIntent = new Intent(MainActivity.this, RecipeNamesActivity.class);
        newIntent.putExtra(ITEM_NAME, item);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
