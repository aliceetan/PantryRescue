package com.ait.kim.pantryrescue.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ait.kim.pantryrescue.MainActivity;
import com.ait.kim.pantryrescue.R;
import com.ait.kim.pantryrescue.data.Item;
import com.ait.kim.pantryrescue.touch.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by kimpham on 12/10/17.
 */

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private Button btnDeleteItem;


        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnDeleteItem = (Button) itemView.findViewById(R.id.btnDeleteItem);
            itemView.setClickable(true);


        }
    }

    private List<Item> itemList;
    private Context context;
    private Realm realmItem;

    public ItemRecyclerAdapter(Context context, Realm realmItem) {
        this.context = context;
        this.realmItem = realmItem;


        itemList = new ArrayList<Item>();

        RealmResults<Item> items = realmItem.where(Item.class).findAll().sort("itemName", Sort.ASCENDING);

        for (Item item : items) {
            itemList.add(item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row,
                viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Item itemData = itemList.get(position);
        holder.tvName.setText(itemData.getItemName());
        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemDismiss(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).showRecipeActivity(itemData.getItemName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void onItemDismiss(int index) {
        ((MainActivity) context).deleteItem(itemList.get(index));
        itemList.remove(index);
        notifyItemRemoved(index);

    }

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


    public void addItem(String item) {

        realmItem.beginTransaction();

        Item newItem = realmItem.createObject(Item.class, UUID.randomUUID().toString());
        newItem.setItemName(item);
        realmItem.commitTransaction();

        itemList.add(0, newItem);
        notifyItemInserted(0);
    }


}

