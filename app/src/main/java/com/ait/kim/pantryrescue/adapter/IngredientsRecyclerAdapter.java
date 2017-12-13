package com.ait.kim.pantryrescue.adapter;


//public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView tvName;
//        private ImageButton btnDeleteItem;
//
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tvName = (TextView) itemView.findViewById(R.id.tvName);
//            btnDeleteItem = (ImageButton) itemView.findViewById(R.id.btnDeleteItem);
//            itemView.setClickable(true);
//        }
//    }
//
//    private List<Item> itemList;
//    private Context context;
//    private Realm realmItem;
//
//    public IngredientsRecyclerAdapter(Context context, Realm realmItem) {
//        this.context = context;
//        this.realmItem = realmItem;
//
//
//        itemList = new ArrayList<Item>();
//
//        RealmResults<Item> items = realmItem.where(Item.class).findAll().sort("itemName", Sort.ASCENDING);
//
//        for (Item item : items) {
//            itemList.add(item);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row,
//                viewGroup, false);
//
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        final Item itemData = itemList.get(position);
//        holder.tvName.setText(itemData.getItemName());
//        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemDismiss(holder.getAdapterPosition());
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MainActivity) context).showRecipeActivity(itemData.getItemName());
//            }
//        });
//    }


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ait.kim.pantryrescue.MainActivity;
import com.ait.kim.pantryrescue.R;
import com.ait.kim.pantryrescue.data.Item;
import com.ait.kim.pantryrescue.data.Post;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private List<String> ingredientKeys;
    private String uId;
    private int lastPosition = -1;
    private DatabaseReference ingredientRef;

    public IngredientsRecyclerAdapter(Context context, String uId){
        this.context = context;
        this.uId = uId;

        itemList = new ArrayList<Item>();
        ingredientKeys = new ArrayList<String>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Item ingredient = itemList.get(position);
        holder.tvName.setText(ingredient.getItemName());

        if (uId.equals(ingredient.getUid())) {
            holder.btnDeleteItem.setVisibility(View.VISIBLE);
        } else {
            holder.btnDeleteItem.setVisibility(View.INVISIBLE);
        }

        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).showRecipeActivity(ingredient.getItemName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void removeItem(int index) {
        ingredientRef = FirebaseDatabase.getInstance().getReference();
        ingredientRef.child("ingredients").child(ingredientKeys.get(index)).removeValue();
        itemList.remove(index);
        ingredientKeys.remove(index);
        notifyItemRemoved(index);
    }

    public void removeItemByKey(String key) {
        int index = ingredientKeys.indexOf(key);
        if (index != -1) {
            itemList.remove(index);
            ingredientKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageButton btnDeleteItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }


    public void addIngredient(Item ingredient, String key){
        itemList.add(ingredient);
        ingredientKeys.add(key);

        notifyDataSetChanged();
    }


    //
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
}