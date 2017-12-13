package com.ait.kim.pantryrescue.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.ait.kim.pantryrescue.adapter.IngredientsRecyclerAdapter;

/**
 * Created by kimpham on 12/10/17.
 */


public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private IngredientsRecyclerAdapter recyclerAdapter;

    public ItemTouchHelperCallback(IngredientsRecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        recyclerAdapter.onItemMove(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        recyclerAdapter.onItemDismiss(viewHolder.getAdapterPosition());

    }
}