package com.ait.kim.pantryrescue;

import android.content.DialogInterface;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ait.kim.pantryrescue.adapter.IngredientsRecyclerAdapter;
import com.ait.kim.pantryrescue.data.Item;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {


    public static final String ITEM_NAME = "ITEM_NAME";
    private IngredientsRecyclerAdapter adapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                Toast.makeText(MainActivity.this, R.string.authors, Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_view:
                                showPostActivity();
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
        CollapsingToolbarLayout collaspingToolbar = findViewById(R.id.toolbar_layout);
        collaspingToolbar.setCollapsedTitleTextAppearance(R.style.RobotoBoldTextAppearance);
        collaspingToolbar.setExpandedTitleTextAppearance(R.style.RobotoBoldTextAppearance);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        setUpList();
        initIngredientsListener();
    }

    private void setUpList(){
        RecyclerView recyclerViewItem = (RecyclerView) findViewById(R.id.recyclerItem);
        adapter = new IngredientsRecyclerAdapter(this, FirebaseAuth.getInstance().getCurrentUser().getUid());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewItem.setLayoutManager(layoutManager);
        recyclerViewItem.setAdapter(adapter);
    }

    private void initIngredientsListener() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ingredients");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item ingredient = dataSnapshot.getValue(Item.class);
                adapter.addIngredient(ingredient,dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.removeItemByKey(dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.dialogtext);
        final EditText input = new EditText(MainActivity.this);
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(MainActivity.this, R.string.inputerror, Toast.LENGTH_SHORT).show();
                } else {
                    String title = input.getText().toString();
                    addToList(title);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    private void addToList(String title){
        String newKey = FirebaseDatabase.getInstance().getReference().child("ingredients").push().getKey();
        Item newIngredient = new Item(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                title
        );
        FirebaseDatabase.getInstance().getReference().child("ingredients").child(newKey)
                .setValue(newIngredient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPostActivity() {
        Intent newIntent = new Intent(MainActivity.this, PostsActivity.class);
        startActivity(newIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
